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

    public static final String ANDROID_DEVICE = "android";
    public static final String CATEGORIES_PARAM = "categories";
    public static final String PAGE_PARAM = "perPage";
    public static final String CURATED_PARAM = "total_curated";

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES)
    Observable<Response<CategoryHadesModel>> getCategories(@Header("X-Device") String device, @Path("catId") String categoryId,
                                                           @QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES)
    Observable<Response<CategoryHadesModel>> getCategories(@Header("X-Device") String device, @Path("catId") String categoryId);

}
