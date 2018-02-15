package com.tokopedia.core.network.core;

/**
 * Created by ricoharisin on 2/28/17.
 */

@Deprecated
public class OkHttpRetryPolicy {

    public int readTimeout;
    public int writeTimeout;
    public int connectTimeout;
    public int maxRetryAttempt;


    public OkHttpRetryPolicy(int readTimeout, int writeTimeout, int connectTimeout, int maxRetryAttempt) {
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.connectTimeout = connectTimeout;
        this.maxRetryAttempt = maxRetryAttempt;
    }


    public static OkHttpRetryPolicy createdDefaultOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(45, 45, 45, 3);
    }

    public static OkHttpRetryPolicy createdOkHttpNoAutoRetryPolicy() {
        return new OkHttpRetryPolicy(45, 45, 45, 0);
    }

    public static OkHttpRetryPolicy createdOkHttpRetryPolicyQuickTimeOut() {
        return new OkHttpRetryPolicy(1, 1, 1, 0);
    }

    public static OkHttpRetryPolicy createdOkHttpRetryPolicyMediumTimeOut() {
        return new OkHttpRetryPolicy(3, 3, 3, 0);
    }

    public static OkHttpRetryPolicy createdOkHttpRetryPolicyQuickNoRetry() {
        return new OkHttpRetryPolicy(45, 45, 45, 0);
    }
}
