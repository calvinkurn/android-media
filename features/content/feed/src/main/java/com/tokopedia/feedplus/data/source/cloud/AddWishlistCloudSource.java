package com.tokopedia.feedplus.data.source.cloud;

import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.feedplus.data.mapper.AddWishlistMapper;
import com.tokopedia.feedplus.data.mapper.RemoveWishlistMapper;
import com.tokopedia.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.feedplus.domain.model.wishlist.RemoveWishlistDomain;
import com.tokopedia.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistCloudSource {

    private AddWishlistMapper addWishlistMapper;
    private RemoveWishlistMapper removeWishlistMapper;
    private MojitoNoRetryAuthService service;


    public AddWishlistCloudSource(MojitoNoRetryAuthService service,
                                  AddWishlistMapper addWishlistMapper,
                                  RemoveWishlistMapper removeWishlistMapper) {
        this.addWishlistMapper = addWishlistMapper;
        this.removeWishlistMapper = removeWishlistMapper;
        this.service = service;
    }


    public Observable<AddWishlistDomain> addWishlist(RequestParams requestParams) {
        return service.getApi()
                .addWishlist(
                        requestParams.getString(AddWishlistUseCase.PARAM_PRODUCT_ID, ""),
                        requestParams.getString(AddWishlistUseCase.PARAM_USER_ID, ""))
                .map(addWishlistMapper);
    }

    public Observable<RemoveWishlistDomain> removeWishlist(RequestParams requestParams) {
        return service.getApi()
                .removeWishlist(
                        requestParams.getString(RemoveWishlistUseCase.PARAM_PRODUCT_ID, ""),
                        requestParams.getString(RemoveWishlistUseCase.PARAM_USER_ID, ""))
                .map(removeWishlistMapper);
    }
}
