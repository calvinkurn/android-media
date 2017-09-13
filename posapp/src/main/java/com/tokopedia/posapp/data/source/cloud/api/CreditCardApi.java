package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.posapp.PosConstants;
import com.tokopedia.posapp.data.pojo.BankInstallmentResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 9/5/17.
 */

public interface CreditCardApi {
    @GET(TkpdBaseURL.Payment.PATH_INSTALLMENT_TERMS +
            PosConstants.Payment.MERCHANT_CODE + "/" + PosConstants.Payment.PROFILE_CODE)
    Observable<Response<BankInstallmentResponse>> getBankInstallment(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Payment.PATH_CC_BIN)
    Observable<Response<BankInstallmentResponse>> getBins(@QueryMap Map<String, String> params);
}
