package com.tokopedia.core.network.apiservices.transaction.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface DepositActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_DO_WITHDRAW)
    Observable<Response<TkpdResponse>> doWithDraw(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_SEND_OTP_VERIFY_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> sendOTPVerifyBank(@FieldMap Map<String, String> params);

}
