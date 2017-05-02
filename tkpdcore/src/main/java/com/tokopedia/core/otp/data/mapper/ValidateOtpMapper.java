package com.tokopedia.core.otp.data.mapper;

import com.tokopedia.core.network.entity.phoneverification.ValidateOtpData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.otp.data.ValidateOtpModel;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 */
public class ValidateOtpMapper implements Func1<Response<TkpdResponse>, ValidateOtpModel> {
    @Override
    public ValidateOtpModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ValidateOtpModel mappingResponse(Response<TkpdResponse> response) {
        ValidateOtpModel model = new ValidateOtpModel();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ValidateOtpData data = response.body().convertDataObj(ValidateOtpData.class);
                model.setSuccess(true);
                model.setValidateOtpData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    model.setSuccess(false);
                    model.setErrorMessage(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
        } else {
            model.setSuccess(false);
        }
        model.setResponseCode(response.code());
        return model;
    }
}