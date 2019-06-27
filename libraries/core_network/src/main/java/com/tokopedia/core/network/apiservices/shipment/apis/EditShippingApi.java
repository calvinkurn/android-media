package com.tokopedia.core.network.apiservices.shipment.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Kris on 2/22/2016.
 */

@Deprecated
public interface EditShippingApi {

    @GET(TkpdBaseURL.Shop.PATH_GET_SHIPPING_INFO)
    Observable<Response<TkpdResponse>> getShippingList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Shop.PATH_GET_DETAIL_INFO_DETAIL)
    Observable<String> getShippingWebViewDetail(@QueryMap Map<String, String> param);

}
