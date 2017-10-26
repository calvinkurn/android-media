package com.tokopedia.digital.tokocash.entity;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ItemHistoryEntity {

    private long transaction_id;

    private long transaction_detail_id;

    private String transaction_type;

    private String title;

    private String icon_uri;

    private String description;

    private String transaction_info_id;

    private String transaction_info_date;

    private String amount_changes;

    private String amount_changes_symbol;

    private long amount;

    private String notes;

    private String amount_pending;

    private List<ActionHistoryEntity> actions;

    public long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public long getTransaction_detail_id() {
        return transaction_detail_id;
    }

    public void setTransaction_detail_id(long transaction_detail_id) {
        this.transaction_detail_id = transaction_detail_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransaction_info_id() {
        return transaction_info_id;
    }

    public void setTransaction_info_id(String transaction_info_id) {
        this.transaction_info_id = transaction_info_id;
    }

    public String getTransaction_info_date() {
        return transaction_info_date;
    }

    public void setTransaction_info_date(String transaction_info_date) {
        this.transaction_info_date = transaction_info_date;
    }

    public String getAmount_changes() {
        return amount_changes;
    }

    public void setAmount_changes(String amount_changes) {
        this.amount_changes = amount_changes;
    }

    public String getAmount_changes_symbol() {
        return amount_changes_symbol;
    }

    public void setAmount_changes_symbol(String amount_changes_symbol) {
        this.amount_changes_symbol = amount_changes_symbol;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public List<ActionHistoryEntity> getActions() {
        return actions;
    }

    public void setActions(List<ActionHistoryEntity> actions) {
        this.actions = actions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmount_pending() {
        return amount_pending;
    }

    public void setAmount_pending(String amount_pending) {
        this.amount_pending = amount_pending;
    }
}