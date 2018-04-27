package com.tokopedia.otp.cotp.domain.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.otp.cotp.domain.pojo.RequestOtpPojo;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class RequestOtpMapper implements Func1<Response<String>, RequestOtpViewModel> {

    private static final String MESSAGE_STATUS = "message_status";
    private static final String DATA = "data";
    private static final String MESSAGE_ERROR = "message_error";

    @Inject
    public RequestOtpMapper() {
    }

    @Override
    public RequestOtpViewModel call(Response<String> response) {
        try {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = getJsonData(jsonResponse);

            String messageError = getMessageError(jsonResponse);
            String messageStatus = getMessageStatus(jsonResponse);

            if (response.isSuccessful()
                    && TextUtils.isEmpty(messageError)
                    && jsonData != null) {

                Gson gson = new Gson();
                RequestOtpPojo pojo = gson.fromJson(jsonData.toString(), RequestOtpPojo.class);

                return new RequestOtpViewModel(pojo.isSuccess(), messageStatus);

            } else if (!TextUtils.isEmpty(messageError)) {
                throw new RuntimeException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }
    }

    private String getMessageStatus(JSONObject jsonResponse) throws JSONException {
        if (!jsonResponse.isNull(MESSAGE_STATUS)) {
            JSONArray jArray = jsonResponse.getJSONArray(MESSAGE_STATUS);
            if (jArray != null) {
                return jArray.getString(0);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private JSONObject getJsonData(JSONObject jsonResponse) throws JSONException {
        if (!jsonResponse.isNull(DATA)) {
            return jsonResponse.getJSONObject(DATA);
        } else {
            return null;
        }
    }

    private String getMessageError(JSONObject jsonResponse) throws JSONException {
        if (!jsonResponse.isNull(MESSAGE_ERROR)) {
            JSONArray jArray = jsonResponse.getJSONArray(MESSAGE_ERROR);
            if (jArray != null) {
                return jArray.getString(0);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
