package com.tokopedia.core.network.retrofit;

import okhttp3.OkHttpClient;

/**
 * @author anggaprasetiyo on 2/28/17.
 */
public class HttpClientFactory {
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static OkHttpClient getInstanceOkHttpClient() {
        return okHttpClient;
    }

    private HttpClientFactory() {
    }
}
