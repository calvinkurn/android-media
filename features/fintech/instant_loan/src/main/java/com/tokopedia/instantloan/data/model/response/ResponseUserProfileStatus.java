package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 6/5/18.
 */

public class ResponseUserProfileStatus {

    @SerializedName("data")
    @Expose
    private UserProfileLoanEntity userProfileLoanEntity;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("latency")
    @Expose
    private String latency;

    public UserProfileLoanEntity getUserProfileLoanEntity() {
        return userProfileLoanEntity;
    }

    public void setUserProfileLoanEntity(UserProfileLoanEntity userProfileLoanEntity) {
        this.userProfileLoanEntity = userProfileLoanEntity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }
}
