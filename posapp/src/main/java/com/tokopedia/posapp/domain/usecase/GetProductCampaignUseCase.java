package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.product.model.productdetail.ProductCampaign;
import com.tokopedia.posapp.data.repository.ProductRepository;

import rx.Observable;

/**
 * Created by okasurya on 8/11/17.
 */

public class GetProductCampaignUseCase extends UseCase<ProductCampaign> {
    ProductRepository productRepository;

    public GetProductCampaignUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductCampaign> createObservable(RequestParams requestParams) {
        return productRepository.getProductCampaign(requestParams);
    }
}
