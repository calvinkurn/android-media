package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.feedplus.domain.model.wishlist.RemoveWishlistDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public interface WishlistRepository {

    Observable<AddWishlistDomain> addWishlist(RequestParams requestParams);

    Observable<RemoveWishlistDomain> removeWishlist(RequestParams requestParams);
}
