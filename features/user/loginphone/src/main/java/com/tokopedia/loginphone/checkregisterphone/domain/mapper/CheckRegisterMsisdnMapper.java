package com.tokopedia.loginphone.checkregisterphone.domain.mapper;

import com.tokopedia.loginphone.checkregisterphone.domain.pojo.CheckRegisterPhoneNumberPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by yfsx on 28/02/18.
 */

public class CheckRegisterMsisdnMapper implements Func1<Response<DataResponse<CheckRegisterPhoneNumberPojo>>,
        CheckRegisterPhoneNumberPojo> {

    @Inject
    public CheckRegisterMsisdnMapper() {
    }

    @Override
    public CheckRegisterPhoneNumberPojo call(Response<DataResponse<CheckRegisterPhoneNumberPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

}