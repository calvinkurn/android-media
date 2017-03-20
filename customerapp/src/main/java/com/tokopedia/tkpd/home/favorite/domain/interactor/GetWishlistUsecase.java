package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Kulomady on 1/18/17.
 */

public class GetWishlistUsecase extends UseCase<DomainWishlist> {

    public static final String KEY_COUNT = "count";
    public static final String KEY_PAGE = "page";
    public static final String DEFAULT_PAGE_VALUE = "1";
    public static final String DEFAULT_COUNT_VALUE = "4";

    private final FavoriteRepository favoriteRepository;

    @Inject
    public GetWishlistUsecase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              FavoriteRepository favoriteRepository) {

        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Observable<DomainWishlist> createObservable(RequestParams requestParams) {
        return favoriteRepository.getWishlist(requestParams.getParameters());
    }
}
