package com.tokopedia.loginphone.verifyotptokocash.domain.mapper;

import com.tokopedia.loginphone.verifyotptokocash.domain.pojo.verifyotp.VerifyOtpTokoCashPojo;
import com.tokopedia.network.data.model.response.DataResponse;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpTokoCashMapper implements Func1<Response<DataResponse<VerifyOtpTokoCashPojo>>,
        VerifyOtpTokoCashPojo> {

    @Inject
    public VerifyOtpTokoCashMapper() {
    }

    @Override
    public VerifyOtpTokoCashPojo call(Response<DataResponse<VerifyOtpTokoCashPojo>> response) {
        if (response.isSuccessful()) {
            return response.body().getData();
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }
}
