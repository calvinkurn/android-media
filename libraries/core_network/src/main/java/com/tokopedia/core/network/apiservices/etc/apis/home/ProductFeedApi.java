package com.tokopedia.core.network.apiservices.etc.apis.home;

import com.tokopedia.core.network.entity.home.GetListFaveShopId;
import com.tokopedia.core.network.entity.home.ProductFeedData;
import com.tokopedia.core.network.entity.home.ProductFeedData2;
import com.tokopedia.core.network.entity.home.ProductFeedData3;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by m.normansyah on 26/11/2015.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface ProductFeedApi {

    String GET_LIST_FAVE_SHOP_ID = "/v4/home/get_list_fave_shop_id.pl";
    String LIMIT = "Limit";
    String SEARCH_V1_PRODUCT = "/search/v1/product";
    String SEARCH_V2_4_PRODUCT = "/search/v2.4/product";
    String GET_RECENT_VIEW_PRODUCT_PL = "get_recent_view_product.pl";

    @GET(GET_RECENT_VIEW_PRODUCT_PL)
    Observable<Response<ProductFeedData>> getLastSeenProduct(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,
            @Query(NetworkCalculator.DEVICE_ID) String deviceId,
            @Query(NetworkCalculator.HASH) String hash,
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime);

    @GET(GET_RECENT_VIEW_PRODUCT_PL)
    Observable<Response<ProductFeedData>> getLastSeenProduct(@QueryMap Map<String, String> params);

    @GET(SEARCH_V1_PRODUCT)
    Observable<ProductFeedData2> getProductFeed(
            @Query("shop_id") String shopId
    );

    @GET(SEARCH_V1_PRODUCT)
    Observable<ProductFeedData2> getProductFeed2(
            @Query("shop_id") String shopId,
            @Query("rows") String rows,
            @Query("start") String start
    );

    @GET(SEARCH_V2_4_PRODUCT)
    Observable<ProductFeedData3> getProductFeed3(
            @Query("shop_id") String shopId,
            @Query("rows") String rows,
            @Query("start") String start,
            @Query("device") String device,
            @Query("ob") String ob,
            @Query("user_id") String userId
    );

    @FormUrlEncoded
    @POST(GET_LIST_FAVE_SHOP_ID)
    Observable<GetListFaveShopId> getListFaveShopId(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(GET_LIST_FAVE_SHOP_ID)
    Observable<GetListFaveShopId> getListFaveShopId(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Field(NetworkCalculator.USER_ID) String userId,// 5
            @Field(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Field(NetworkCalculator.HASH) String hash,// 7
            @Field(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Field(LIMIT) String limit
    );


}
