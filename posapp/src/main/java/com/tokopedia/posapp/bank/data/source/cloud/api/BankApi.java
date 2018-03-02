package com.tokopedia.posapp.bank.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by okasurya on 10/17/17.
 */

public interface BankApi {

    String MERCHANT_CODE = "merchant_code";
    String PROFILE_CODE = "profile_code";

    @GET(TkpdBaseURL.Pos.GET_INSTALLMENT_TERM)
    Observable<Response<TkpdResponse>> getBankInstallment(
            @Path(MERCHANT_CODE) String merchantCode,
            @Path(PROFILE_CODE) String profileCode
    );

    @GET(TkpdBaseURL.Pos.GET_CREDIT_CARDS)
    Observable<Response<TkpdResponse>> getBins();
}
