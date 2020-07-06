package com.tokopedia.topads.sdk.utils;

import android.os.AsyncTask;

import com.tokopedia.topads.sdk.listener.ImpressionListener;
import com.tokopedia.topads.sdk.network.HttpMethod;
import com.tokopedia.topads.sdk.network.HttpRequest;
import com.tokopedia.topads.sdk.network.RawHttpRequestExecutor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;


/**
 * @author by errysuprayogi on 7/12/17.
 */
public class ImpresionTask extends AsyncTask<String, Void, String> {

    private static final String KEY_SESSION_ID = "Tkpd-SessionID";

    private ImpressionListener impressionListener;
    private ImpressionTaskAlert taskAlert;
    private UserSessionInterface userSession;

    public ImpresionTask(String className) {
        taskAlert = ImpressionTaskAlert.getInstance(className);
    }

    public ImpresionTask(String className, ImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
        taskAlert = ImpressionTaskAlert.getInstance(className);
    }

    public ImpresionTask(String className, UserSessionInterface userSession) {
        this.userSession = userSession;
        taskAlert = ImpressionTaskAlert.getInstance(className);
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        if (url != null) {
            try {
                if (taskAlert != null) {
                    taskAlert.track(url);
                }
                HttpRequest request = new HttpRequest.HttpRequestBuilder()
                        .setBaseUrl(url)
                        .addHeader(KEY_SESSION_ID, (userSession != null) ? userSession.getDeviceId() : "")
                        .setMethod(HttpMethod.GET)
                        .build();
                return RawHttpRequestExecutor.newInstance(request).executeAsGetRequest();
            } catch (IOException | RuntimeException e) {
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
