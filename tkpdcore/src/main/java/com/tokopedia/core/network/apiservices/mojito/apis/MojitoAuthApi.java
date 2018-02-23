package com.tokopedia.core.network.apiservices.mojito.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * MojitoAuthApi
 * Created by ricoharisin on 4/15/16.
 */
public interface MojitoAuthApi {

    @DELETE(TkpdBaseURL.Mojito.PATH_PRODUCT + "{userId}/"
            + TkpdBaseURL.Mojito.PATH_WISHLIST + "{productId}/"
            + TkpdBaseURL.Mojito.PATH_WISH_LIST_V_1_1)
    Observable<Response<Void>> deleteWishlist(
            @Path("productId") String productId, @Path("userId") String userId
    );

    @DELETE(TkpdBaseURL.Mojito.PATH_PRODUCT + "{userId}/"
            + TkpdBaseURL.Mojito.PATH_WISHLIST + "{productId}/"
            + TkpdBaseURL.Mojito.PATH_WISH_LIST_V_1_1)
    Observable<Response<TkpdResponse>> removeWishlist(
            @Path("productId") String productId, @Path("userId") String userId
    );

    @POST(TkpdBaseURL.Mojito.PATH_PRODUCT + "{userId}/"
            + TkpdBaseURL.Mojito.PATH_WISHLIST + "{productId}/"
            + TkpdBaseURL.Mojito.PATH_WISH_LIST_V_1_1)
    Observable<Response<TkpdResponse>> addWishlist(
            @Path("productId") String productId, @Path("userId") String userId
    );

    @GET(TkpdBaseURL.Mojito.PATH_USER_RECENT_VIEW + "{userId}" + TkpdBaseURL.Mojito.PATH_RECENT_VIEW)
    Observable<Response<RecentViewData>> getRecentViews(@Path("userId") String UserId);

    @GET(TkpdBaseURL.Mojito.PATH_WISHLIST_PRODUCT)
    Observable<Response<WishlistData>> getWishlist(@Path("userId") String UserId,
                                                   @Query("count") int Count,
                                                   @Query("page") int Page);

    @GET(TkpdBaseURL.Mojito.PATH_RECENT_VIEW_UPDATE)
    Observable<Response<TkpdResponse>> updateRecentView(
            @Query("product_id") String productId, @Query("user_id") String userId
    );
}
