package com.tokopedia.topads.sdk.utils;

import android.os.AsyncTask;

import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import java.io.IOException;

/**
 * @author by errysuprayogi on 7/12/17.
 */
public class ImpresionTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpRequest request = new HttpRequest.HttpRequestBuilder()
                .setBaseUrl(params[0])
                .setMethod(HttpMethod.GET)
                .build();
        try {
            return RawHttpRequestExecutor.newInstance(request).executeAsGetRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
