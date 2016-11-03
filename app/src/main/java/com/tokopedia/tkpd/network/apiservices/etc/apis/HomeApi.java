package com.tokopedia.tkpd.network.apiservices.etc.apis;

import com.tokopedia.tkpd.home.model.network.GetListFaveShopId;
import com.tokopedia.tkpd.home.model.network.ProductFeedData;
import com.tokopedia.tkpd.home.model.network.TopAdsData;
import com.tokopedia.tkpd.home.model.network.WishlistData;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

import static com.tokopedia.tkpd.home.api.ProductFeedApi.GET_LIST_FAVE_SHOP_ID;

/**
 * HomeApi
 * Created by Angga.Prasetiyo on 08/12/2015.
 */
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
