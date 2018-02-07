package com.tokopedia.digital.tokocash.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.digital.tokocash.entity.WalletTokenEntity;
import com.tokopedia.digital.tokocash.errorhandle.ResponseTokoCashRuntimeException;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 8/2/17.
 */

public class TokenTokoCashMapper implements Func1<Response<TkpdResponse>, WalletTokenEntity> {

    @Override
    public WalletTokenEntity call(Response<TkpdResponse> tkpdResponseResponse) {
        return mappingResponse(tkpdResponseResponse);
    }

    private WalletTokenEntity mappingResponse(Response<TkpdResponse> response) {
        WalletTokenEntity model = new WalletTokenEntity();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                WalletTokenEntity data = response.body().convertDataObj(WalletTokenEntity.class);
                model.setToken(data.getToken());
            } else {
                if (response.body().getErrorMessages() != null &&
                        response.body().getErrorMessages().equals("invalid_request")){
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
