package com.tokopedia.changephonenumber.data.mapper;


import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.changephonenumber.data.model.ValidateNumberData;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberMapper implements Func1<Response<TokopediaWsV4Response>, Boolean> {
    @Inject
    public ValidateNumberMapper() {
    }

    @Override
    public Boolean call(Response<TokopediaWsV4Response> tkpdResponseResponse) {
        boolean model = false;
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                ValidateNumberData data = tkpdResponseResponse.body().convertDataObj(
                        ValidateNumberData.class);
                model = (data.getIsSuccess() == 1);
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    Observable.error(new MessageErrorException(tkpdResponseResponse.body().getErrorMessageJoined()));
                } else {
                    throw new RuntimeException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}
