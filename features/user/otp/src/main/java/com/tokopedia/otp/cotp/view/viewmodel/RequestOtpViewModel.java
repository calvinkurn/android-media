package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 10/23/17.
 */

public class RequestOtpViewModel {
    private boolean isSuccess;
    private String messageStatus;
    private String phoneHint;

    public RequestOtpViewModel(boolean isSuccess, String messageStatus, String phoneHint) {
        this.isSuccess = isSuccess;
        this.messageStatus = messageStatus;
        this.phoneHint = phoneHint;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getPhoneHint() { return phoneHint; }
}

