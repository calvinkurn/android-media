package com.tokopedia.posapp.bank.data.source.cloud.api;

import com.tokopedia.posapp.bank.data.pojo.BankItemResponse;
import com.tokopedia.posapp.bank.data.pojo.CCBinResponse;
import com.tokopedia.posapp.base.data.pojo.ListResponse;
import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.common.PosUrl;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public interface BankApi {
    String MERCHANT_CODE = "merchant_code";
    String PROFILE_CODE = "profile_code";

    @GET(PosUrl.Payment.GET_INSTALLMENT_TERM)
    Observable<Response<PosSimpleResponse<ListResponse<BankItemResponse>>>> getBankInstallment(
            @Path(MERCHANT_CODE) String merchantCode,
            @Path(PROFILE_CODE) String profileCode
    );

    @GET(PosUrl.Payment.GET_CREDIT_CARDS)
    Observable<Response<PosSimpleResponse<ListResponse<CCBinResponse>>>> getBins();
}
