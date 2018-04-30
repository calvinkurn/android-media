package com.tokopedia.otp.cotp.domain.mapper;

import com.tokopedia.otp.common.network.OtpErrorException;
import com.tokopedia.otp.common.network.WsResponse;
import com.tokopedia.otp.cotp.domain.pojo.MakeLoginPojo;
import com.tokopedia.otp.cotp.view.viewmodel.OtpLoginDomain;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 4/25/18.
 */

public class MakeLoginMapper implements Func1<Response<WsResponse<MakeLoginPojo>>, OtpLoginDomain> {
    private static final String TRUE_1 = "1";
    private static final String TRUE = "true";

    @Inject
    public MakeLoginMapper() {
    }

    @Override
    public OtpLoginDomain call(Response<WsResponse<MakeLoginPojo>> response) {

        if (response.isSuccessful()
                && response.body() != null
                && response.body().getMessageError() == null) {

            MakeLoginPojo pojo = response.body().getData();
            return convertToDomain(pojo);

        } else if (response.body() != null && response.body().getMessageError() != null) {
            throw new OtpErrorException(response.body().getMessageError().get(0));
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
    }

    private OtpLoginDomain convertToDomain(MakeLoginPojo pojo) {
        return new OtpLoginDomain(pojo.getShopIsGold(),
                pojo.getMsisdnIsVerified().equals(TRUE_1),
                pojo.getShopId(), pojo.getShopName(),
                pojo.getFullName(),
                pojo.getIsLogin().equals(TRUE),
                pojo.getUserId());
    }

}
