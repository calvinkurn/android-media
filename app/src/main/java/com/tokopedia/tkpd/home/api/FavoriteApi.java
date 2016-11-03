package com.tokopedia.tkpd.home.api;

import com.tokopedia.tkpd.home.model.ShopDataModel;
import com.tokopedia.tkpd.home.model.TopAdsHome;
import com.tokopedia.tkpd.home.model.network.FavoriteSendData;
import com.tokopedia.tkpd.home.model.network.TopAdsData;
import com.tokopedia.tkpd.home.model.network.WishlistData;
import com.tokopedia.tkpd.home.presenter.Favorite;
import com.tokopedia.tkpd.home.presenter.FavoriteImpl;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.utils.NetworkCalculator;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by m.normansyah on 27/11/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface FavoriteApi {

    @GET(TkpdBaseURL.TopAds.URL_TOPADS + TkpdBaseURL.TopAds.PATH_DISPLAY_SHOP)
    Observable<Response<TopAdsHome>> getTopAdsApi(
            @Header("Tkpd-UserId") String contentMD5,// 1
            @Header("Tkpd-SessionId") String tkpdSessionId,// 2
            @Header("X-Device") String xDevice, // 3
            @Query("item") String item,// 4
            @Query("src") String src, // 5
            @Query("page") String page// 6
    );

    @GET(TkpdBaseURL.Etc.PATH_GET_WISHLIST)
    Observable<Response<WishlistData>> getWishList(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Query(Favorite.QUERY) String query,// 9
            @Query(Favorite.PER_PAGE_KEY) String perpage,// 10
            @Query(Favorite.PAGE_KEY) String page);// 11

    @GET(TkpdBaseURL.Product.PATH_AD_SHOP_FEED)
    Observable<TopAdsData> getTopAds(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime// 8
    );

    @GET(TkpdBaseURL.Etc.PATH_GET_FAVORITE_SHOP)
    Observable<TopAdsData> getFavorite(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Query(Favorite.OPTION_LOCATION) String optionLocation,
            @Query(Favorite.OPTION_NAME) String optionName,
            @Query(Favorite.PER_PAGE_KEY) String perPage,
            @Query(Favorite.PAGE_KEY) String page
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<ShopDataModel> getShopInfo(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Field(NetworkCalculator.USER_ID) String userId,// 5
            @Field(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Field(NetworkCalculator.HASH) String hash,// 7
            @Field(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Field(FavoriteImpl.SHOP_ID_KEY) String shopId
    );

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_FAVE_SHOP)
    Observable<FavoriteSendData> sendFavorite(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Field(FavoriteImpl.SHOP_ID_KEY) String shopId,
            @Field(FavoriteImpl.AD_KEY) String ad_Key,
            @Field(FavoriteImpl.SRC) String src
    );

}
