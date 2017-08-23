package com.tokopedia.digital.tokocash.model;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ItemHistory {

    private long transactionId;

    private long transactionDetailId;

    private String transactionType;

    private String title;

    private String description;

    private String transactionInfoId;

    private String transactionInfoDate;

    private String amountChanges;

    private String amountChangesSymbol;

    private long amount;

    private List<ActionHistory> actionHistoryList;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(long transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionInfoId() {
        return transactionInfoId;
    }

    public void setTransactionInfoId(String transactionInfoId) {
        this.transactionInfoId = transactionInfoId;
    }

    public String getTransactionInfoDate() {
        return transactionInfoDate;
    }

    public void setTransactionInfoDate(String transactionInfoDate) {
        this.transactionInfoDate = transactionInfoDate;
    }

    public String getAmountChanges() {
        return amountChanges;
    }

    public void setAmountChanges(String amountChanges) {
        this.amountChanges = amountChanges;
    }

    public String getAmountChangesSymbol() {
        return amountChangesSymbol;
    }

    public void setAmountChangesSymbol(String amountChangesSymbol) {
        this.amountChangesSymbol = amountChangesSymbol;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public List<ActionHistory> getActionHistoryList() {
        return actionHistoryList;
    }

    public void setActionHistoryList(List<ActionHistory> actionHistoryList) {
        this.actionHistoryList = actionHistoryList;
    }
}
