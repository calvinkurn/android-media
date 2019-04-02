package com.tokopedia.gm.subscribe.domain.product.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.subscribe.domain.product.GmSubscribeProductRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 2/9/17.
 */
public class ClearGmSubscribeProductCacheUseCase extends UseCase<Boolean> {
    private final GmSubscribeProductRepository gmSubscribeProductReposistory;

    @Inject
    public ClearGmSubscribeProductCacheUseCase(GmSubscribeProductRepository gmSubscribeProductReposistory) {
        super();
        this.gmSubscribeProductReposistory = gmSubscribeProductReposistory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gmSubscribeProductReposistory.clearGMProductCache();
    }
}
