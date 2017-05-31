package com.tokopedia.core.network.apiservices.hades.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Alifa on 2/22/2017.
 */

public interface HadesApi {

    String ANDROID_DEVICE = "android";

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES)
    Observable<Response<CategoryHadesModel>> getCategories(@Header("X-Device") String device, @Path("catId") String categoryId,
                                                           @QueryMap Map<String, String> params);

}
