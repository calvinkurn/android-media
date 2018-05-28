package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.feedplus.data.repository.WishlistRepository;
import com.tokopedia.feedplus.domain.model.wishlist.AddWishlistDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public class AddWishlistUseCase extends UseCase<AddWishlistDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_PRODUCT_ID = "PARAM_PRODUCT_ID";
    private WishlistRepository wishlistRepository;

    @Inject
    public AddWishlistUseCase(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Observable<AddWishlistDomain> createObservable(RequestParams requestParams) {
        return wishlistRepository.addWishlist(requestParams);
    }

    public static RequestParams generateParam(String productId, UserSession userSession) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userSession.getUserId());
        params.putString(PARAM_PRODUCT_ID, productId);
        return params;
    }
}
