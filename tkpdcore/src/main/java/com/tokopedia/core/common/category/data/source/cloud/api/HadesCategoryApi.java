package com.tokopedia.core.common.category.data.source.cloud.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryVersionServiceModel;
import com.tokopedia.core.common.category.data.source.cloud.model.CategoryServiceModel;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface HadesCategoryApi {
    @GET(TkpdBaseURL.HadesCategory.CHECK_VERSION)
    Observable<Response<CategoryVersionServiceModel>> checkVersion();

    @GET(TkpdBaseURL.HadesCategory.FETCH_CATEGORIES)
    Observable<Response<CategoryServiceModel>> fetchCategory();
}
