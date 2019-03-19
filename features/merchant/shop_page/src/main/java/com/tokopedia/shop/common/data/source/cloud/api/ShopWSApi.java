package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopWSApi {

    @GET(ShopUrl.SHOP_FAVOURITE_USER)
    Observable<Response<DataResponse<ShopFavouritePagingList<ShopFavouriteUser>>>> getShopFavouriteUserList(@QueryMap Map<String, String> params);
}
