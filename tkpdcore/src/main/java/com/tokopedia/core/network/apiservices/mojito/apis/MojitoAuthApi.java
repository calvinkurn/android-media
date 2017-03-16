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

    @DELETE(TkpdBaseURL.Mojito.PATH_PRODUCT+"{productId}/"+TkpdBaseURL.Mojito.PATH_WISHLIST)
    Observable<Response<Void>> deleteWishlist(@Path("productId") String productId);

    @DELETE(TkpdBaseURL.Mojito.PATH_PRODUCT+"{productId}/"+TkpdBaseURL.Mojito.PATH_WISHLIST)
    Observable<Response<TkpdResponse>> removeWishlist(@Path("productId") String productId);

    @POST(TkpdBaseURL.Mojito.PATH_PRODUCT+"{productId}/"+TkpdBaseURL.Mojito.PATH_WISHLIST)
    Observable<Response<TkpdResponse>> addWishlist(@Path("productId") String productId);

    @GET(TkpdBaseURL.Mojito.PATH_USER_RECENT_VIEW + "{userId}" + TkpdBaseURL.Mojito.PATH_RECENT_VIEW)
    Observable<Response<RecentViewData>> getRecentViews(@Path("userId") String UserId);

    @GET(TkpdBaseURL.Mojito.PATH_WISHLIST_PRODUCT)
    Observable<Response<WishlistData>> getWishlist(@Path("userId") String UserId,
                                                   @Query("count") int Count,
                                                   @Query("page") int Page);
}
