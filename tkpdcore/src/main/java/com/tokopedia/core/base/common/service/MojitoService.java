package com.tokopedia.core.base.common.service;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface MojitoService {

    @GET(TkpdBaseURL.Mojito.PATH_USER_RECENT_VIEW + "{userId}" + TkpdBaseURL.Mojito.PATH_RECENT_VIEW)
    Observable<Response<String>> getRecentProduct(@Path("userId") String UserId);

    @GET(TkpdBaseURL.Mojito.PATH_USER + "{userId}/" + TkpdBaseURL.Mojito.PATH_WISHLIST_PRODUCT)
    Observable<Response<String>> getWishlist(@Path("userId") String UserId,
                                             @QueryMap HashMap<String, String> params);
}
