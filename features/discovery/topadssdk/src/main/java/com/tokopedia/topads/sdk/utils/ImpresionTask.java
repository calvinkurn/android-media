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

    private static final String KEY_SESSION_ID ="Tkpd-SessionID";

    private ImpressionListener impressionListener;

    private UserSessionInterface userSession;

    public ImpresionTask() {
    }

    public ImpresionTask(ImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
    }

    public ImpresionTask(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        if(url!=null) {
            HttpRequest request = new HttpRequest.HttpRequestBuilder()
                    .setBaseUrl(url)
                    .addHeader(KEY_SESSION_ID, (userSession != null) ? userSession.getDeviceId() :"")
                    .setMethod(HttpMethod.GET)
                    .build();
            try {
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
