package com.tokopedia.tokocash.historytokocash.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ItemHistory implements Parcelable {

    private long transactionId;

    private long transactionDetailId;

    private String transactionType;

    private String title;

    private String urlImage;

    private String description;

    private String transactionInfoId;

    private String transactionInfoDate;

    private String amountChanges;

    private String amountChangesSymbol;

    private long amount;

    private String notes;

    private String amountPending;

    private List<ActionHistory> actionHistoryList;

    public ItemHistory() {
    }


    protected ItemHistory(Parcel in) {
        transactionId = in.readLong();
        transactionDetailId = in.readLong();
        transactionType = in.readString();
        title = in.readString();
        urlImage = in.readString();
        description = in.readString();
        transactionInfoId = in.readString();
        transactionInfoDate = in.readString();
        amountChanges = in.readString();
        amountChangesSymbol = in.readString();
        amount = in.readLong();
        notes = in.readString();
        amountPending = in.readString();
        actionHistoryList = in.createTypedArrayList(ActionHistory.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(transactionId);
        dest.writeLong(transactionDetailId);
        dest.writeString(transactionType);
        dest.writeString(title);
        dest.writeString(urlImage);
        dest.writeString(description);
        dest.writeString(transactionInfoId);
        dest.writeString(transactionInfoDate);
        dest.writeString(amountChanges);
        dest.writeString(amountChangesSymbol);
        dest.writeLong(amount);
        dest.writeString(notes);
        dest.writeString(amountPending);
        dest.writeTypedList(actionHistoryList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemHistory> CREATOR = new Creator<ItemHistory>() {
        @Override
        public ItemHistory createFromParcel(Parcel in) {
            return new ItemHistory(in);
        }

        @Override
        public ItemHistory[] newArray(int size) {
            return new ItemHistory[size];
        }
    };

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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmountPending() {
        return amountPending;
    }

    public void setAmountPending(String amountPending) {
        this.amountPending = amountPending;
    }
}