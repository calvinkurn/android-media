package com.tokopedia.core.network.apiservices.referral.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by ashwanityagi on 08/11/17.
 */

@Deprecated
public interface ReferralApi {

    @POST(TkpdBaseURL.Referral.PATH_GET_REFERRAL_VOUCHER_CODE)
    @Headers({"Content-Type: application/json"})
    Observable<String> getReferralVoucherCode(@Body String params);
}