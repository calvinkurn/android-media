package com.tokopedia.tkpd.home.wishlist.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.wishlist.domain.model.DataWishlist;

import rx.Observable;

/**
 * @author Kulomady on 2/20/17.
 */

public class SearchWishlistUsecase extends UseCase<DataWishlist>{
    public static final String KEY_QUERY = "query";
    public static final String KEY_USER_ID = "userId";

    private final WishlistRepository wishlistRepository;

    public SearchWishlistUsecase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 WishlistRepository wishlistRepository) {
        super(threadExecutor, postExecutionThread);
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public Observable<DataWishlist> createObservable(RequestParams requestParams) {
        String query = requestParams.getString(KEY_QUERY, "");
        String userId = requestParams.getString(KEY_USER_ID, "");
        return wishlistRepository.search_wishlist(userId, query);
    }
}
