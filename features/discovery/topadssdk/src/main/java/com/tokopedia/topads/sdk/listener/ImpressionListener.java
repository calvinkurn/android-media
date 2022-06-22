package com.tokopedia.topads.sdk.listener;

public interface ImpressionListener {

    void onSuccess();

    void onSuccess(String header);

    void onFailed();
}
