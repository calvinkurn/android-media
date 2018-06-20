package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 10/23/17.
 */

public class RequestOtpViewModel {
    private boolean isSuccess;
    private String messageStatus;

    public RequestOtpViewModel(boolean isSuccess, String messageStatus) {
        this.isSuccess = isSuccess;
        this.messageStatus = messageStatus;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessageStatus() {
        return messageStatus;
    }
}
