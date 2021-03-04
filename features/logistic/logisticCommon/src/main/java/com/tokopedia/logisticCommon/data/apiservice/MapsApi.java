package com.tokopedia.logisticCommon.data.apiservice;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.network.constant.TkpdBaseURL;
import com.tokopedia.network.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
public interface MapsApi {

    @GET(TkpdBaseURL.Maps.PATH_MAPS_PLACES)
    Observable<Response<TokopediaWsV4Response>> getRecommendedPlaces(
            @QueryMap TKPDMapParam<String, Object> params
    );

    @GET(TkpdBaseURL.Maps.PATH_MAPS_PLACES_DETAIL)
    Observable<Response<TokopediaWsV4Response>> getLatLng(
            @QueryMap TKPDMapParam<String, Object> params
    );

    @GET(TkpdBaseURL.Maps.PATH_MAPS_GEOCODE)
    Observable<Response<TokopediaWsV4Response>> getLatLngGeocode(
            @QueryMap TKPDMapParam<String, Object> params
    );

}
