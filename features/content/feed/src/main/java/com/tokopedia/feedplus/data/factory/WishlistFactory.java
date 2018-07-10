package com.tokopedia.feedplus.data.factory;

import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.feedplus.data.mapper.AddWishlistMapper;
import com.tokopedia.feedplus.data.mapper.RemoveWishlistMapper;
import com.tokopedia.feedplus.data.source.cloud.AddWishlistCloudSource;

import javax.inject.Inject;

/**
 * @author by nisie on 5/30/17.
 */

public class WishlistFactory {

    private AddWishlistMapper addWishlistMapper;
    private RemoveWishlistMapper removeWishlistMapper;
    private MojitoNoRetryAuthService service;

    @Inject
    public WishlistFactory(AddWishlistMapper addWishlistMapper,
                           RemoveWishlistMapper removeWishlistMapper,
                           MojitoNoRetryAuthService service) {
        this.addWishlistMapper = addWishlistMapper;
        this.removeWishlistMapper = removeWishlistMapper;
        this.service = service;
    }

    public AddWishlistCloudSource createCloudWishlistSource() {
        return new AddWishlistCloudSource(
                service,
                addWishlistMapper,
                removeWishlistMapper);
    }

}
