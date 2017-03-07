package com.tokopedia.tkpd.home.wishlist.data;

import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.tkpd.home.wishlist.domain.WishlistRepository;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import rx.Observable;

/**
 * @author Kulomady on 2/20/17.
 */

public class WishlistDataRepository implements WishlistRepository{
    private final MojitoService mojitoService;
    private WishlistDataMapper wishlistDataMapper;

    public WishlistDataRepository() {
        this.mojitoService = new MojitoService();
        this.wishlistDataMapper = new WishlistDataMapper();
    }

    @Override
    public Observable<DataWishlist> search_wishlist(String userId, String query) {
        return new CloudWishlistDatasource(mojitoService, wishlistDataMapper).searchWishlist(userId, query);
    }
}
