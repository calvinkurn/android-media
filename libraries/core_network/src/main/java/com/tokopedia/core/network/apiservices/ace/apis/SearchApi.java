package com.tokopedia.core.network.apiservices.ace.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.hotlist.HotListResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */

@Deprecated
public interface SearchApi {

    @GET(TkpdBaseURL.Ace.PATH_HOTLIST_CATEGORY)
    Observable<Response<HotListResponse>> getHotlistCategory(@QueryMap Map<String, String> params);
}
