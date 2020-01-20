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

public class RequestOtpMapper implements Func1<Response<WsResponse<RequestOtpPojo>>, RequestOtpViewModel> {

    private static final String OTP_CODE_SENT = "Kode OTP berhasil dikirim";

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
                return new RequestOtpViewModel(
                        pojo.getIsSuccess() == SUCCESS,
                        response.body().getMessageStatus().get(0),
                        pojo.getClientView().getPhoneHint());
            } else {
                return new RequestOtpViewModel(
                        pojo.getIsSuccess() == SUCCESS,
                        OTP_CODE_SENT,
                        pojo.getClientView().getPhoneHint());
            }

        } else if (response.body() != null && response.body().getMessageError() != null) {
            throw new OtpErrorException(response.body().getMessageError().get(0));
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }

    }
}
