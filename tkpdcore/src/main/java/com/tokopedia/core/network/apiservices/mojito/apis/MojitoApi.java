package com.tokopedia.core.network.apiservices.mojito.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.wishlist.WishlistData;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ricoharisin on 4/15/16.
 * Modified by Mady add HomeCategoryMenu
 */
public interface MojitoApi {

    String DEVICE = "device";

    @GET(TkpdBaseURL.Mojito.PATH_WISHLIST_PRODUCT)
    Observable<Response<WishlistData>> getWishlist(@Path("userId") String UserId,
                                                   @Query("count") int Count,
                                                   @Query("page") int Page);

    //api requirement add static header
    @Headers({
            "X-Device: android"})
    @GET(TkpdBaseURL.Mojito.API_HOME_CATEGORY_MENU)
    Observable<Response<String>> getHomeCategoryMenu();

    @GET(TkpdBaseURL.Mojito.API_V1_BRANDS)
    Observable<Response<Brands>> getBrands(
            @Query(DEVICE) String device
    );

    @GET(TkpdBaseURL.Mojito.PATH_SEARCH_WISHLIST)
    Observable<Response<WishlistData>> searchWishlist(
            @Path("userId") String userId,
            @Query("q") String query,
            @Query("page") int page,
            @Query("count") int count);
}
