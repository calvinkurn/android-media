package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 6/5/18.
 */

public class UserProfileLoanEntity {

    public UserProfileLoanEntity(){

    }
    public UserProfileLoanEntity(Boolean isWhitelist, String whiteListUrl,
                                 Boolean dataCollection, Boolean dataCollected) {
        this.isWhitelist = isWhitelist;
        this.whiteListUrl = whiteListUrl;
        this.dataCollection = dataCollection;
        this.dataCollected = dataCollected;
    }

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
}
