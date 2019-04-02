package com.tokopedia.core.network.core;

/**
 * Created by ricoharisin on 2/28/17.
 */

@Deprecated
public class OkHttpRetryPolicy extends com.tokopedia.abstraction.common.network.OkHttpRetryPolicy {

    public OkHttpRetryPolicy(int readTimeout, int writeTimeout, int connectTimeout, int maxRetryAttempt) {
        super(readTimeout, writeTimeout, connectTimeout, maxRetryAttempt);
    }


    public static OkHttpRetryPolicy createdDefaultOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(100, 100, 100, 3);
    }

    public static OkHttpRetryPolicy createdOkHttpNoAutoRetryPolicy() {
        return new OkHttpRetryPolicy(45, 45, 45, 0);
    }

    public static OkHttpRetryPolicy createdOkHttpRetryPolicyQuickTimeOut() {
        return new OkHttpRetryPolicy(1, 1, 1, 0);
    }

    public static OkHttpRetryPolicy createdOkHttpRetryPolicyQuickNoRetry() {
        return new OkHttpRetryPolicy(45, 45, 45, 0);
    }
}
