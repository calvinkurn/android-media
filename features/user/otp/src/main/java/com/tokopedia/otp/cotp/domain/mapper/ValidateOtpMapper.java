package com.tokopedia.otp.cotp.domain.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.otp.cotp.domain.pojo.ValidateOtpPojo;
import com.tokopedia.otp.cotp.domain.pojo.ValidateOtpSQPojo;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpMapper implements Func1<Response<String>, ValidateOtpDomain> {
    private static final String DATA = "data";
    private static final String MESSAGE_ERROR = "message_error";
    private static final String UUID = "uuid";

    @Inject
    public ValidateOtpMapper() {
    }

    @Override
    public ValidateOtpDomain call(Response<String> response) {

        try {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = getJsonData(jsonResponse);

            String messageError = getMessageError(jsonResponse);

            if (response.isSuccessful()
                    && TextUtils.isEmpty(messageError)
                    && jsonData != null) {

                Gson gson = new Gson();
                if (responseIsSecurityQuestion(response.body())) {
                    ValidateOtpSQPojo validateOtpSQData = gson.fromJson(jsonData.toString(),
                            ValidateOtpSQPojo.class);
                    return convertToDomain(validateOtpSQData.isSuccess(), validateOtpSQData.getUuid());
                } else {
                    ValidateOtpPojo validateOtpData = gson.fromJson(jsonData.toString(),
                            ValidateOtpPojo.class);
                    return convertToDomain(validateOtpData.isSuccess(), "");
                }

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

    private boolean responseIsSecurityQuestion(String body) {
        return body.contains(UUID);
    }

    private ValidateOtpDomain convertToDomain(boolean isSuccess, String uuid) {
        return new ValidateOtpDomain(isSuccess, uuid);
    }
}
