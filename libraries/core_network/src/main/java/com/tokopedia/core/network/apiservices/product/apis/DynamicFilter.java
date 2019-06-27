package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * DynamicFilter
 * Created by noiz354 on 7/11/16.
 */
@Deprecated
public interface DynamicFilter {
    String DEVICE_ID = "device_id";
    String DEVICE_TIME = "device_time";
    String HASH = "hash";
    String DEVICE = "device";
    String SOURCE = "source";
    String QUERY = "q";
    String USER_ID = "user_id";
    String V1_DYNAMIC_ATTRIBUTES = "v2/dynamic_attributes";
    String DYNAMIC_FILTER_URL = TkpdBaseURL.ACE_DOMAIN;
    String fullUrl = DYNAMIC_FILTER_URL + V1_DYNAMIC_ATTRIBUTES;
    String SC = "sc";

    /*
    https://ace.tokopedia.com/v1/dynamic_attributes?
    device_id=1EDFBBA8-029E-4E86-AA19-1E7649B103D6
    &device_time=1467353877.593733
    &hash=93d26f9eb4b2adc1e966e5c737ded734
    &os_type=2
    &source=search_product
    &user_id=1299609
     */
    @GET(V1_DYNAMIC_ATTRIBUTES)
    Observable<Response<DynamicFilterModel>> browseCatalogs(@QueryMap Map<String, String> stringMap);
}
