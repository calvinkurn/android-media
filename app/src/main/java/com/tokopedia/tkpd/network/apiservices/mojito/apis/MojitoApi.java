package com.tokopedia.tkpd.network.apiservices.mojito.apis;

import com.tokopedia.tkpd.home.model.homeMenu.HomeCategoryMenuItem;
import com.tokopedia.tkpd.home.model.wishlist.WishlistData;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ricoharisin on 4/15/16.
 * Modified by Mady add HomeCategoryMenu
 */
public interface MojitoApi {

    @GET(TkpdBaseURL.Mojito.PATH_USER+"{userId}/"+TkpdBaseURL.Mojito.PATH_WISHLIST_PRODUCT)
    Observable<Response<WishlistData>> getWishlist(@Path("userId") String UserId, @Query("count") int Count, @Query("page") int Page);

    //api requirement add static header
    @Headers({
            "X-Device: android"})
    @GET(TkpdBaseURL.Mojito.API_HOME_CATEGORY_MENU)
    Observable<Response<HomeCategoryMenuItem>> getHomeCategoryMenu();

}
