package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpDomain {

    private boolean isSuccess;
    private String uuid;
    private String msisdn;


    public ValidateOtpDomain(boolean isSuccess, String uuid, String msisdn) {
        this.isSuccess = isSuccess;
        this.uuid = uuid;
        this.msisdn = msisdn;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMsisdn() {
        return msisdn;
    }
}
