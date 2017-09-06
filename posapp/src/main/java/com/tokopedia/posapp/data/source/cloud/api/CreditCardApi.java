package com.tokopedia.posapp.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.PosConstants;

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
    Observable<Response<TkpdResponse>> getBankInstallment(@QueryMap Map<String, String> params);
}
