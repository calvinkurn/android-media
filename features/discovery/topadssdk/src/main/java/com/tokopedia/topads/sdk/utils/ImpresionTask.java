package com.tokopedia.topads.sdk.utils;

import android.os.AsyncTask;

import com.tokopedia.entertainment.home.alert.ImpressionTaskAlert;
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
    private ImpressionTaskAlert taskAlert;

    public ImpresionTask(Class aClass) {
        taskAlert = ImpressionTaskAlert.getInstance(aClass);
    }

    public ImpresionTask(Class aClass, ImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
        taskAlert = ImpressionTaskAlert.getInstance(aClass);
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
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
            taskAlert.track(url);
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
