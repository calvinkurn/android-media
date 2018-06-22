package com.tokopedia.core.network.apiservices.tokocash;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdDigitalResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by nabillasabbaha on 10/11/17.
 */

public interface WalletLoginApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Wallet.REQUEST_OTP_LOGIN)
    Observable<Response<TkpdDigitalResponse>> requestLoginOtp(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Wallet.VERIFY_OTP_LOGIN)
    Observable<Response<TkpdDigitalResponse>> verifyLoginOtp(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Wallet.AUTHORIZE)
    Observable<Response<TkpdDigitalResponse>> getAuthorizeCode(@FieldMap Map<String,
            Object> parameters);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Wallet.CHECK_MSISDN)
    Observable<Response<TkpdDigitalResponse>> checkMsisdn(@FieldMap Map<String,
            Object> parameters);
}
