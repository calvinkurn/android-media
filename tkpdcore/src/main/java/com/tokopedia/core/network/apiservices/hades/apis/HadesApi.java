package com.tokopedia.core.network.apiservices.hades.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.categories.Data;
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
    String CATEGORIES_PARAM = "categories";
    String DEVICE_PARAM = "X-Device";
    String CATEGORY_ID_PARAM = "catId";
    String PAGE_PARAM = "perPage";
    String CURATED_PARAM = "total_curated";

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES)
    Observable<Response<CategoryHadesModel>> getCategories(@Header(DEVICE_PARAM) String device, @Path(CATEGORY_ID_PARAM) String categoryId,
                                                           @QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES)
    Observable<Response<CategoryHadesModel>> getCategories(@Header(DEVICE_PARAM) String device, @Path(CATEGORY_ID_PARAM) String categoryId);

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES_LAYOUT)
    Observable<Response<Data>> getNavigationCategories(@Path(CATEGORY_ID_PARAM) String categoryId);

    @GET(TkpdBaseURL.HadesCategory.PATH_CATEGORIES_LAYOUT_ROOT)
    Observable<Response<Data>> getNavigationCategoriesRoot(@Path(CATEGORY_ID_PARAM) String categoryId);
}
