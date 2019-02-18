package com.tokopedia.saldodetails.response.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DepositActivityResponse {

    @SerializedName("message")
    private
    String message;

    @SerializedName("have_error")
    private
    boolean haveError;

    @SerializedName("have_next_page")
    private
    boolean haveNextPage;

    @SerializedName("deposit_history_list")
    private
    List<DepositHistoryList> depositHistoryList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHaveError() {
        return haveError;
    }

    public void setHaveError(boolean haveError) {
        this.haveError = haveError;
    }

    public boolean isHaveNextPage() {
        return haveNextPage;
    }

    public void setHaveNextPage(boolean haveNextPage) {
        this.haveNextPage = haveNextPage;
    }

    public List<DepositHistoryList> getDepositHistoryList() {
        return depositHistoryList;
    }

    public void setDepositHistoryList(List<DepositHistoryList> depositHistoryList) {
        this.depositHistoryList = depositHistoryList;
    }
}
