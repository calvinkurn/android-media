package com.tokopedia.shop.favourite.data.repository;

import com.tokopedia.shop.favourite.data.source.ShopFavouriteDataSource;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouritePagingList;
import com.tokopedia.shop.favourite.data.source.cloud.model.ShopFavouriteUser;
import com.tokopedia.shop.favourite.domain.model.ShopFavouriteRequestModel;
import com.tokopedia.shop.favourite.domain.repository.ShopFavouriteRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class ShopFavouriteRepositoryImpl implements ShopFavouriteRepository {
    private final ShopFavouriteDataSource shopFavouriteDataSource;

    @Inject
    public ShopFavouriteRepositoryImpl(ShopFavouriteDataSource shopFavouriteDataSource) {
        this.shopFavouriteDataSource = shopFavouriteDataSource;
    }

    @Override
    public Observable<ShopFavouritePagingList<ShopFavouriteUser>> getShopFavouriteUserList(ShopFavouriteRequestModel shopFavouriteRequestModel) {
        return shopFavouriteDataSource.getShopFavouriteUserList(shopFavouriteRequestModel);
    }
}