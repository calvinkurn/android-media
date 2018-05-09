package com.tokopedia.logisticdata.data.apiservice;

import com.tokopedia.logisticdata.data.constant.LogisticDataConstantUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Irfan Khoirul on 22/03/18.
 */

public interface RatesApi {

    @GET(LogisticDataConstantUrl.KeroRates.PATH_RATES_V2)
    Observable<Response<String>> calculateShippingRate(
            @QueryMap Map<String, String> stringStringMap
    );

}
