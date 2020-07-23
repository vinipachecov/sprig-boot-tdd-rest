package com.bookstore.libraryapi.service;

import com.bookstore.libraryapi.model.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private  final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    @Value("${application.mail.lateloans.message}")
    private String message;

    private final LoanService loanService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public void sendMailToLateLoans() {
        List<Loan> allLateLoans = loanService.getAllLateloans();
        List<String> mailsList = allLateLoans.stream()
        .map(loan -> loan.getCustomerEmail())
        .collect(Collectors.toList());

        emailService.sendMails(message, mailsList);

    }
}
