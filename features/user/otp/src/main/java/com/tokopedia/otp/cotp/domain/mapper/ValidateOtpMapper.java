package com.tokopedia.otp.cotp.domain.mapper;

import com.tokopedia.otp.common.network.OtpErrorException;
import com.tokopedia.otp.common.network.WsResponse;
import com.tokopedia.otp.cotp.domain.pojo.ValidateOtpPojo;
import com.tokopedia.otp.cotp.view.viewmodel.ValidateOtpDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpMapper implements Func1<Response<WsResponse<ValidateOtpPojo>>,
        ValidateOtpDomain> {
    private static final int IS_SUCCESS = 1;

    @Inject
    public ValidateOtpMapper() {
    }

    @Override
    public ValidateOtpDomain call(Response<WsResponse<ValidateOtpPojo>> response) {

        if (response.isSuccessful()
                && response.body() != null
                && response.body().getMessageError() == null) {

            ValidateOtpPojo pojo = response.body().getData();
            return convertToDomain(pojo.getIsSuccess() == IS_SUCCESS,
                    pojo.getUuid() != null ? pojo.getUuid() : "",
                    pojo.getMsisdn() != null ? pojo.getMsisdn() : ""
                    );

        } else if (response.body() != null && response.body().getMessageError() != null) {
            throw new OtpErrorException(response.body().getMessageError().get(0));
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private ValidateOtpDomain convertToDomain(boolean isSuccess, String uuid, String msisdn) {
        return new ValidateOtpDomain(isSuccess, uuid, msisdn);
    }
}
