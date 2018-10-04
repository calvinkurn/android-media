package com.tokopedia.shop.favourite.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class ShopFavouriteCloudDataSource {

    private final ShopWSApi shopWS4Api;

    @Inject
    public ShopFavouriteCloudDataSource(ShopWSApi shopWS4Api) {
        this.shopWS4Api = shopWS4Api;
    }

    public Observable<Response<DataResponse<ShopFavouritePagingList<ShopFavouriteUser>>>> getShopFavouriteUserList(
            ShopFavouriteRequestModel shopFavouriteRequestModel) {
        return shopWS4Api.getShopFavouriteUserList(shopFavouriteRequestModel.getHashMap());
    }
}
