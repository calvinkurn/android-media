package com.tokopedia.core.network.apiservices.maps.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by kris on 9/14/17. Tokopedia
 */

public interface MapApi {

    /*@GET(TkpdBaseURL.Maps.PATH_MAPS_PLACES)
    Observable<ArrayList<Prediction>> getRecommendedPlaces(String query);*/

    @GET(TkpdBaseURL.Maps.PATH_MAPS_PLACES)
    Observable<Response<TkpdResponse>> getRecommendedPlaces(
            @QueryMap TKPDMapParam<String, Object> params
    );

    @GET(TkpdBaseURL.Maps.PATH_MAPS_PLACES_DETAIL)
    Observable<Response<TkpdResponse>> getLatLng(
            @QueryMap TKPDMapParam<String, Object> params
    );

    @GET(TkpdBaseURL.Maps.PATH_MAPS_GEOCODE)
    Observable<Response<TkpdResponse>> getLatLngGeocode(
            @QueryMap TKPDMapParam<String, Object> params
    );

}
