package com.tokopedia.feedplus.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.feedplus.data.repository.WishlistRepository;
import com.tokopedia.feedplus.domain.model.wishlist.RemoveWishlistDomain;

import rx.Observable;

/**
 * @author by nisie on 5/30/17.
 */

public class RemoveWishlistUseCase extends UseCase<RemoveWishlistDomain> {

    public static final String PARAM_USER_ID = "PARAM_USER_ID";
    public static final String PARAM_PRODUCT_ID = "PARAM_PRODUCT_ID";
    private WishlistRepository wishlistRepository;

    public RemoveWishlistUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 WishlistRepository wishlistRepository) {
        super(threadExecutor, postExecutionThread);
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Observable<RemoveWishlistDomain> createObservable(RequestParams requestParams) {
        return wishlistRepository.removeWishlist(requestParams);
    }

    public static RequestParams generateParam(String productId, SessionHandler sessionHandler) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, sessionHandler.getLoginID());
        params.putString(PARAM_PRODUCT_ID, productId);
        return params;
    }
}
