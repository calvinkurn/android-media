package com.tokopedia.recentview.data.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.recentview.data.entity.RecentViewData;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface RecentViewApi {

    @GET(RecentViewUrl.GET_RECENT_VIEW_URL)
    Observable<Response<DataResponse<RecentViewData>>> getRecentProduct(@Path("userId") String UserId);
}
