package com.tokopedia.loyalty.domain.apiservice;

import com.tokopedia.loyalty.domain.model.TkpdDigitalResponse;
import com.tokopedia.network.constant.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Rizky on 12/7/17.
 */

public interface DigitalApi {

    @GET(TkpdBaseURL.DigitalApi.VERSION + TkpdBaseURL.DigitalApi.PATH_CHECK_VOUCHER)
    Observable<Response<TkpdDigitalResponse>> checkVoucher(@QueryMap Map<String, String> params);

}
