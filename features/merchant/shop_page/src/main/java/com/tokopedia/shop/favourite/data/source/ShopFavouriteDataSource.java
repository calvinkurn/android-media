package com.tokopedia.shop.favourite.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.favourite.data.source.cloud.ShopFavouriteCloudDataSource;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class ShopFavouriteDataSource {
    private ShopFavouriteCloudDataSource favouriteCloudDataSource;

    @Inject
    public ShopFavouriteDataSource(ShopFavouriteCloudDataSource shopNoteCloudDataSource) {
        this.favouriteCloudDataSource = shopNoteCloudDataSource;
    }

    public Observable<ShopFavouritePagingList<ShopFavouriteUser>> getShopFavouriteUserList(ShopFavouriteRequestModel shopFavouriteRequestModel) {
        return favouriteCloudDataSource.getShopFavouriteUserList(shopFavouriteRequestModel).flatMap(new Func1<Response<DataResponse<ShopFavouritePagingList<ShopFavouriteUser>>>, Observable<ShopFavouritePagingList<ShopFavouriteUser>>>() {
            @Override
            public Observable<ShopFavouritePagingList<ShopFavouriteUser>> call(Response<DataResponse<ShopFavouritePagingList<ShopFavouriteUser>>> dataResponse) {
                return Observable.just(dataResponse.body().getData());
            }
        });
    }
}
