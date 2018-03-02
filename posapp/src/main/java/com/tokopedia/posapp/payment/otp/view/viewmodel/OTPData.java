package com.tokopedia.posapp.payment.otp.view.viewmodel;

/**
 * Created by okasurya on 10/5/17.
 */

public class OTPData {
    private String url;
    private String method;
    private String gateway;
    private byte[] parameters;
    private OTPDetailTransaction otpDetailTransaction;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public OTPDetailTransaction getOtpDetailTransaction() {
        return otpDetailTransaction;
    }

    public void setOtpDetailTransaction(OTPDetailTransaction otpDetailTransaction) {
        this.otpDetailTransaction = otpDetailTransaction;
    }
}
