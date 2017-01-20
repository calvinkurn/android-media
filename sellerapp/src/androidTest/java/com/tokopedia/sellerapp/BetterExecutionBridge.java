package com.tokopedia.sellerapp;

/**
 * Created by pt2121 on 3/7/15.
 */
public interface BetterExecutionBridge {

    void onStart();

    void onError();

    void onEnd();
}
