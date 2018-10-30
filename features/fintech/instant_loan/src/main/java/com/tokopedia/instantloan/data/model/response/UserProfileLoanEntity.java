package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 6/5/18.
 */

public class UserProfileLoanEntity {


    @SerializedName("whitelist")
    @Expose
    private boolean isWhitelist;

    @SerializedName("data_collection")
    @Expose
    private boolean dataCollection;

    @SerializedName("whitelist_url")
    @Expose
    private String whiteListUrl;

    @SerializedName("data_collected")
    @Expose
    private boolean dataCollected;

    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;

    @SerializedName("on_going_loan_id")
    @Expose
    private int onGoingLoanId;

    public boolean getWhitelist() {
        return isWhitelist;
    }

    public void setWhitelist(boolean whitelist) {
        isWhitelist = whitelist;
    }

    public boolean getDataCollection() {
        return dataCollection;
    }

    public void setDataCollection(boolean dataCollection) {
        this.dataCollection = dataCollection;
    }

    public boolean getDataCollected() {
        return dataCollected;
    }

    public void setDataCollected(boolean dataCollected) {
        this.dataCollected = dataCollected;
    }

    public String getWhiteListUrl() {
        return whiteListUrl;
    }

    public void setWhiteListUrl(String whiteListUrl) {
        this.whiteListUrl = whiteListUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getOnGoingLoanId() {
        return onGoingLoanId;
    }

    public void setOnGoingLoanId(int onGoingLoanId) {
        this.onGoingLoanId = onGoingLoanId;
    }
}
