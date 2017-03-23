package com.tokopedia.tkpd.home.wishlist.data;

import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author Kulomady on 2/20/17.
 */

public class CloudWishlistDatasource {

    private MojitoService mojitoService;
    private WishlistDataMapper wishlistDataMapper;

    public CloudWishlistDatasource(MojitoService mojitoService,
                                   WishlistDataMapper wishlistDataMapper) {

        this.mojitoService = mojitoService;
        this.wishlistDataMapper = wishlistDataMapper;
    }

    public Observable<DataWishlist> searchWishlist(String userId, String query) {
        return mojitoService.getApi().searchWishlist(userId, query)
                .debounce(150, TimeUnit.MICROSECONDS)
                .map(wishlistDataMapper);
    }
}
