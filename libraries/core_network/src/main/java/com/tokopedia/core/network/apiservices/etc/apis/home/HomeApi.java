package com.tokopedia.core.network.apiservices.etc.apis.home;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.GetListFaveShopId;
import com.tokopedia.core.network.entity.home.ProductFeedData;
import com.tokopedia.core.network.entity.home.TopAdsData;
import com.tokopedia.core.network.entity.home.WishlistData;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.core.network.apiservices.etc.apis.home.ProductFeedApi.GET_LIST_FAVE_SHOP_ID;


/**
 * HomeApi
 * Created by Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public interface HomeApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_FAVORITE_SHOP)
    Observable<TopAdsData> getFavoriteShop(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_PRODUCT_FEED)
    Observable<Response<TkpdResponse>> getProductFeed(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Etc.PATH_GET_RECENT_VIEW_PRODUCT)
    Observable<Response<ProductFeedData>> getRecentViewProduct(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_WISHLIST)
    Observable<Response<WishlistData>> getWishList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(GET_LIST_FAVE_SHOP_ID)
    Observable<GetListFaveShopId> getListFaveShopId(@FieldMap Map<String, String> params);
}
