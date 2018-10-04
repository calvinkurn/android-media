package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpLoginDomain {

    private ValidateOtpDomain validateOtpDomain;
    private OtpLoginDomain makeLoginDomain;

    public ValidateOtpDomain getValidateOtpDomain() {
        return validateOtpDomain;
    }

    public void setValidateOtpDomain(ValidateOtpDomain validateOtpDomain) {
        this.validateOtpDomain = validateOtpDomain;
    }

    public OtpLoginDomain getMakeLoginDomain() {
        return makeLoginDomain;
    }

    public void setMakeLoginDomain(OtpLoginDomain makeLoginDomain) {
        this.makeLoginDomain = makeLoginDomain;
    }
}
