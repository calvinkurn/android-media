package com.tokopedia.gm.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.gm.featured.repository.GMFeaturedProductRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class GMFeaturedProductGetListUseCase extends UseCase<GMFeaturedProductDomainModel> {

    private GMFeaturedProductRepository GMFeaturedProductRepository;

    @Inject
    public GMFeaturedProductGetListUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           GMFeaturedProductRepository GMFeaturedProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.GMFeaturedProductRepository = GMFeaturedProductRepository;
    }

    @Override
    public Observable<GMFeaturedProductDomainModel> createObservable(RequestParams requestParams) {
        return GMFeaturedProductRepository.getFeatureProductData(requestParams);
    }
}
