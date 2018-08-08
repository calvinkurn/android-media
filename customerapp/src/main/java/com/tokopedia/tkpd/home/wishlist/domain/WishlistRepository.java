package com.tokopedia.tkpd.home.wishlist.domain;

import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import rx.Observable;

/**
 * @author Kulomady on 2/20/17.
 */
public interface WishlistRepository {

    public Observable<DataWishlist> search_wishlist(String userId, String query, int page);

}
