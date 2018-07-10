package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.data.factory.WishlistFactory;
import com.tokopedia.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.feedplus.domain.model.wishlist.RemoveWishlistDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public class WishlistRepositoryImpl implements WishlistRepository {

    private final WishlistFactory wishlistFactory;

    public WishlistRepositoryImpl(WishlistFactory wishlistFactory) {
        this.wishlistFactory = wishlistFactory;
    }

    @Override
    public Observable<AddWishlistDomain> addWishlist(RequestParams requestParams) {
        return wishlistFactory.createCloudWishlistSource().addWishlist(requestParams);
    }

    @Override
    public Observable<RemoveWishlistDomain> removeWishlist(RequestParams requestParams) {
        return wishlistFactory.createCloudWishlistSource().removeWishlist(requestParams);
    }
}
