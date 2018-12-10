package com.tokopedia.loyalty.domain.apiservice;


import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.abstraction.constant.AbstractionBaseURL;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface TXPaymentVoucherApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Transaction.PATH_CHECK_VOUCHER_CODE)
    Observable<Response<TokopediaWsV4Response>> checkVoucherCode(@FieldMap Map<String, String> params);
}
