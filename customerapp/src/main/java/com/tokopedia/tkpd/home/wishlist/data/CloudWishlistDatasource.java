package com.tokopedia.tkpd.home.wishlist.data;

import com.tokopedia.core.network.apiservices.mojito.MojitoService;
import com.tokopedia.core.util.GlobalConfig;
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

    public Observable<DataWishlist> searchWishlist(String userId, String query, int page) {
        return mojitoService.getApi().searchWishlist(userId, query, page, 10, generateDeviceHeader())
                .debounce(150, TimeUnit.MILLISECONDS)
                .map(wishlistDataMapper);
    }

    public String generateDeviceHeader() {
        return "android-"+ GlobalConfig.VERSION_NAME;
    }
}
