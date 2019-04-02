package com.tokopedia.wishlist.common.data.repository;

import com.tokopedia.wishlist.common.data.source.WishListCommonDataSource;
import com.tokopedia.wishlist.common.domain.repository.WishListCommonRepository;

import java.util.List;

import rx.Observable;

/**
 * @author hendry on 4/20/17.
 */

public class WishListCommonRepositoryImpl implements WishListCommonRepository {
    private final WishListCommonDataSource wishListCommonDataSource;

    public WishListCommonRepositoryImpl(WishListCommonDataSource wishListCommonDataSource) {
        this.wishListCommonDataSource = wishListCommonDataSource;
    }

    @Override
    public Observable<List<String>> getWishList(String userId, List<String> productIdList) {
        return wishListCommonDataSource.getWishList(userId, productIdList);
    }

}
