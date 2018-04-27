package com.tokopedia.otp.cotp.domain.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.otp.cotp.domain.pojo.MakeLoginPojo;
import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 4/25/18.
 */

public class MakeLoginMapper implements Func1<Response<String>, OtpLoginDomain> {
    private static final String DATA = "data";
    private static final String MESSAGE_ERROR = "message_error";
    private static final String TRUE_1 = "1";
    private static final String TRUE = "true";

    @Inject
    public MakeLoginMapper() {
    }

    @Override
    public OtpLoginDomain call(Response<String> response) {
        try {
            JSONObject jsonResponse = new JSONObject(response.body());
            JSONObject jsonData = getJsonData(jsonResponse);

            String messageError = getMessageError(jsonResponse);

            if (response.isSuccessful()
                    && TextUtils.isEmpty(messageError)
                    && jsonData != null) {

                Gson gson = new Gson();
                MakeLoginPojo pojo = gson.fromJson(jsonData.toString(), MakeLoginPojo.class);

                return convertToDomain(pojo);

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

    private OtpLoginDomain convertToDomain(MakeLoginPojo pojo) {
        return new OtpLoginDomain(pojo.getShopIsGold(),
                pojo.getMsisdnIsVerified().equals(TRUE_1),
                pojo.getShopId(), pojo.getShopName(),
                pojo.getFullName(),
                pojo.getIsLogin().equals(TRUE),
                pojo.getUserId());
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
