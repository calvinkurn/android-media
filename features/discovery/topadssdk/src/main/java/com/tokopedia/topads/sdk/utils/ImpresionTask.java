package com.tokopedia.topads.sdk.utils;

import android.os.AsyncTask;

import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;

import java.io.IOException;

/**
 * @author by errysuprayogi on 7/12/17.
 */
public class ImpresionTask extends AsyncTask<String, Void, String> {

    private ImpressionListener impressionListener;

    public ImpresionTask() {
    }

    public ImpresionTask(ImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        if(url!=null) {
            HttpRequest request = new HttpRequest.HttpRequestBuilder()
                    .setBaseUrl(url)
                    .setMethod(HttpMethod.GET)
                    .build();
            try {
                return RawHttpRequestExecutor.newInstance(request).executeAsGetRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (impressionListener != null) {
            if (s != null) {
                impressionListener.onSuccess();
            } else {
                impressionListener.onFailed();
            }
        }
    }
}
