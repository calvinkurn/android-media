package com.tokopedia.core.network.apiservices.maps.api;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.tokopedia.core.geolocation.model.Prediction;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.ArrayList;

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
            @QueryMap TKPDMapParam<String, String> params
    );

}
