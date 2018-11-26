package com.tokopedia.otp.cotp.domain.mapper;

import com.tokopedia.otp.common.network.OtpErrorException;
import com.tokopedia.otp.common.network.WsResponse;
import com.tokopedia.otp.cotp.domain.pojo.RequestOtpPojo;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class RequestOtpMapper implements Func1<Response<WsResponse<RequestOtpPojo>>,
        RequestOtpViewModel> {

    @Inject
    public RequestOtpMapper() {
    }

    @Override
    public RequestOtpViewModel call(Response<WsResponse<RequestOtpPojo>> response) {
        if (response.isSuccessful()
                && response.body() != null
                && response.body().getMessageError() == null) {

            RequestOtpPojo pojo = response.body().getData();

            int SUCCESS = 1;
            if (response.body().getMessageStatus() != null
                    && !response.body().getMessageStatus().isEmpty()) {
                return new RequestOtpViewModel(pojo.getIsSuccess() == SUCCESS,
                        response.body().getMessageStatus().get(0));
            } else {
                return new RequestOtpViewModel(pojo.getIsSuccess() == SUCCESS,
                        "Kode OTP berhasil dikirim");
            }

        } else if (response.body() != null && response.body().getMessageError() != null) {
            throw new OtpErrorException(response.body().getMessageError().get(0));
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }
}
