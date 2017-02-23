package com.tokopedia.core.network.apiservices.hades.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Alifa on 2/22/2017.
 */

public interface HadesApi {

    @GET(TkpdBaseURL.Hades.PATH_CATEGORIES)
    Observable<Response<TkpdResponse>> getCategories(String categoryId);

}
