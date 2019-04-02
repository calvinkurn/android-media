package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidateCouponEntity {
    @Expose
    @SerializedName("is_valid")
    private int isValid;

    @Expose
    @SerializedName("message_success")
    private String messageSuccess;

    @Expose
    @SerializedName("message_title")
    private String messageTitle;

    public int isValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public void setMessageSuccess(String messageSuccess) {
        this.messageSuccess = messageSuccess;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    @Override
    public String toString() {
        return "ValidateCoupon{" +
                "isValid=" + isValid +
                ", messageSuccess='" + messageSuccess + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                '}';
    }
}
