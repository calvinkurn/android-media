package com.tokopedia.tkpd.home.favorite.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.model.TopAdsShop;

import rx.Observable;

/**
 * @author Kulomady on 1/19/17.
 */

public class GetTopAdsShopUseCase extends UseCase<TopAdsShop> {
    private final FavoriteRepository favoriteRepository;

    public GetTopAdsShopUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                FavoriteRepository favoriteRepository) {

        super(threadExecutor, postExecutionThread);
        this.favoriteRepository = favoriteRepository;
    }


    @Override
    public Observable<TopAdsShop> createObservable(RequestParams requestParams) {
        return favoriteRepository.getTopAdsShop(requestParams.getParameters());
    }
}
