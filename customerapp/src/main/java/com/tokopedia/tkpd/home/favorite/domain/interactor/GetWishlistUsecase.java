package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.DomainWishlist;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Kulomady on 2/9/17.
 */

@SuppressWarnings("WeakerAccess")
public class GetWishlistUsecase extends UseCase<DomainWishlist> {

    public static final String KEY_COUNT = "count";
    public static final String KEY_PAGE = "page";
    public static final String KEY_IS_FORCE_REFRESH = "isForceUpdate";

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
        boolean isForceRefresh = requestParams.getBoolean(KEY_IS_FORCE_REFRESH, false);
        requestParams.clearValue(KEY_IS_FORCE_REFRESH);
        TKPDMapParam<String, Object> parameters = requestParams.getParameters();
        return favoriteRepository.getWishlist(parameters,isForceRefresh);
    }

    public static RequestParams getDefaultParams() {
        RequestParams param = RequestParams.create();
        param.putString(KEY_PAGE, DEFAULT_PAGE_VALUE);
        param.putString(KEY_COUNT,DEFAULT_COUNT_VALUE);
        return param;
    }
}
