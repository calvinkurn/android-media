package com.tokopedia.withdraw.domain.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoWithdrawPojo {

    @SerializedName("is_success")
    @Expose
    private int isSuccess;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }
}