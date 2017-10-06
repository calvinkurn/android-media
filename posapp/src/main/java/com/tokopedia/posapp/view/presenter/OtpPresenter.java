package com.tokopedia.posapp.view.presenter;

import android.util.Log;

import com.drew.lang.Iterables;
import com.tokopedia.posapp.view.Otp;
import com.tokopedia.posapp.view.viewmodel.OtpData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by okasurya on 10/5/17.
 */

public class OtpPresenter implements Otp.Presenter {
    public static final String UTF_8 = "UTF-8";
    private Otp.View viewListener;

    public OtpPresenter(Otp.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void initializeData(String jsonData) {
        if(jsonData != null) {
            OtpData otpData = new OtpData();
            try {
                JSONObject data = new JSONObject(jsonData);
                otpData.setUrl(data.getString("url"));
                otpData.setMethod(data.getString("method"));
                otpData.setGateway(data.getString("gateway"));
                otpData.setParameters(getQueryParam(data.getJSONObject("form")).getBytes(UTF_8));

                if (otpData.getMethod().equals("POST")) {
                    viewListener.postOTPWebview(otpData);
                } else {
                    viewListener.getOTPWebview(otpData);
                }
            } catch (Exception e) {
                viewListener.onLoadDataError(e);
            }
        }
    }

    private String getQueryParam(JSONObject form) throws JSONException, UnsupportedEncodingException {
        String queryParam = "";
        Iterator<String> keys = form.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if(form.get(key) instanceof String) {
                queryParam += key + "=" + URLEncoder.encode((String) form.get(key), UTF_8);
                if(keys.hasNext()) queryParam += "&";
            } else if(form.get(key) instanceof JSONArray) {
                JSONArray jsonArray = form.getJSONArray(key);
                queryParam += key + "=" + URLEncoder.encode(jsonArray.getString(0), UTF_8);
                if(keys.hasNext()) queryParam += "&";
            }
        }
        return queryParam;
    }
}
