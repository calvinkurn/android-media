package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.core.network.entity.otp.RequestOtpData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;
import com.tokopedia.digital.tokocash.model.ActivateTokoCashData;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 8/2/17.
 */

public class ActivateTokoCashMapper implements Func1<Response<TkpdResponse>, ActivateTokoCashData> {

    @Override
    public ActivateTokoCashData call(Response<TkpdResponse> tkpdResponseResponse) {
        return mappingResponse(tkpdResponseResponse);
    }

    private ActivateTokoCashData mappingResponse(Response<TkpdResponse> response) {
        ActivateTokoCashData model = new ActivateTokoCashData();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                RequestOtpData data = response.body().convertDataObj(RequestOtpData.class);
                model.setSuccess(true);
                model.setRequestOtpData(data);
            } else {
                if (response.body().getErrorMessages() == null
                        && response.body().getErrorMessages().isEmpty()) {
                    model.setSuccess(false);
                } else {
                    throw new ResponseTokoCashRuntimeException(response.body().getErrorMessageJoined());
                }
            }
            model.setStatusMessage(response.body().getStatusMessageJoined());
            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
