package com.tokopedia.payment.model;

public interface TopPayBaseModel {

    String getRedirectUrlToPass();

    String getQueryStringToPass();

    String getCallbackSuccessUrlToPass();

    String getCallbackFailedUrlToPass();

    String getTransactionIdToPass();
}
