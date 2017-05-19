package com.tokopedia.core.network.apiservices.mojito.apis;


import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.Brands;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.core.network.entity.wishlist.WishlistData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.core.product.model.productdetail.ProductCampaignResponse;
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
public interface MojitoApi {

    String DEVICE = "device";
    String ID = "id";

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
            @Query("count") int count,
            @Header("X-Device") String device);

    @GET(TkpdBaseURL.Mojito.PATH_CHECK_WISHLIST)
    Observable<Response<WishlistCheckResult>> checkWishlist(
            @Path("userId") String userId,
            @Path("listId") String listId);

    @GET(TkpdBaseURL.Mojito.PATH_V1_BRAND_CAMPAIGN_DETAIL)
    Observable<Response<ProductCampaignResponse>> getProductCampaign(
            @Query(ID) String id
    );

    @GET(TkpdBaseURL.Mojito.PATH_V1_BRAND_CAMPAIGN_PRODUCT)
    Observable<Response<ShopProductCampaignResponse>> getProductsCampaign(
            @Query("pid") String ids
    );
}
