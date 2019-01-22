package com.tokopedia.payment.router;

public interface IPaymentModuleRouter {

    String getBaseUrlDomainPayment();

    String getGeneratedOverrideRedirectUrlPayment(String originUrl);

    <T> T getGeneratedOverrideRedirectHeaderUrlPayment(String originUrl);

    boolean getEnableFingerprintPayment();

}
