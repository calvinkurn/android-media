package com.tokopedia.phoneverification.domain.mapper;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;
import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.phoneverification.domain.pojo.VerifyPhoneNumberPojo;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberMapper implements Func1<Response<TokopediaWsV4Response>, VerifyPhoneNumberDomain> {

    @Inject
    public VerifyPhoneNumberMapper() {
    }

    @Override
    public VerifyPhoneNumberDomain call(Response<TokopediaWsV4Response> response) {
        VerifyPhoneNumberDomain verifyPhoneNumberDomain = new VerifyPhoneNumberDomain();
        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                VerifyPhoneNumberPojo pojo = response.body().convertDataObj(VerifyPhoneNumberPojo.class);
                verifyPhoneNumberDomain = mappingToViewModel(pojo, response.body().getStatusMessageJoined());
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    Observable.error(new MessageErrorException(response.body().getErrorMessageJoined()));
                } else {
                    throw new RuntimeException("");
                }
            }
        }
        return verifyPhoneNumberDomain;
    }

    private VerifyPhoneNumberDomain mappingToViewModel(VerifyPhoneNumberPojo pojo, String statusMessageJoined) {
        return new VerifyPhoneNumberDomain(pojo.isSuccess(), statusMessageJoined);
    }

}