package com.tokopedia.core.network.apiservices.mojito.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.intermediary.brands.MojitoBrandsModel;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductCampaignResponse;

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

@Deprecated
public interface MojitoApi {

    String DEVICE = "device";
    String ID = "id";
    String PID = "pid";
    String CATEGORY_ID = "categoryId";

    //api requirement add static header
    @Headers({
            "X-Device: android"})
    @GET(TkpdBaseURL.Mojito.API_HOME_CATEGORY_MENU)
    Observable<Response<String>> getHomeCategoryMenu(@Header("X-User-ID") String user_id,
                                                     @Header(AuthUtil.HEADER_X_TKPD_APP_NAME) String appName);

    //api requirement add static header
    @Headers({
            "X-Device: android"})
    @GET(TkpdBaseURL.Mojito.API_HOME_CATEGORY_MENU_V2)
    Observable<Response<String>> getHomeCategoryMenuV2(@Header("X-User-ID") String user_id,
                                                     @Header(AuthUtil.HEADER_X_TKPD_APP_NAME) String appName);


    @GET(TkpdBaseURL.Mojito.API_V2_BRANDS)
    Observable<Response<Brands>> getBrands();

    @GET(TkpdBaseURL.Mojito.API_V1_WISHLIST_COUNT)
    Observable<Response<WishlistCountResponse>> getWishlistCount(@Query("product_id") String productId);

    @GET(TkpdBaseURL.Mojito.API_V2_BRANDS)
    Observable<Response<String>> getBrandsOfficialStore();

    @GET(TkpdBaseURL.Mojito.API_V1_BRANDS_CATEGORY)
    Observable<Response<MojitoBrandsModel>> getBrandsCategory(
            @Path(CATEGORY_ID) String categoryID
    );

    @GET(TkpdBaseURL.Mojito.PATH_SEARCH_WISHLIST)
    Observable<Response<WishlistData>> searchWishlist(
            @Path("userId") String userId,
            @Query("q") String query,
            @Query("page") int page,
            @Query("count") int count,
            @Header("X-Device") String device);

    @GET(TkpdBaseURL.Mojito.PATH_OS_BANNER)
    Observable<Response<BannerOfficialStoreModel>> getOSBanner(
            @Query("keywords") String keyword
    );

    @GET(TkpdBaseURL.Mojito.PATH_OS_BANNER)
    Observable<Response<String>> getOfficialStoreBanner(@Query("keywords") String keyword);

    @GET(TkpdBaseURL.Mojito.PATH_CHECK_WISHLIST)
    Observable<Response<WishlistCheckResult>> checkWishlist(
            @Path("userId") String userId,
            @Path("listId") String listId);

    @GET(TkpdBaseURL.Mojito.PATH_V1_BRAND_CAMPAIGN_PRODUCT)
    Observable<Response<ShopProductCampaignResponse>> getProductCampaigns(
            @Query(PID) String ids
    );
}
