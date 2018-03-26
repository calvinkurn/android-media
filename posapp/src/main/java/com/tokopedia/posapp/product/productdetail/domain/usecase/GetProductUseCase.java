package com.tokopedia.posapp.product.productdetail.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class GetProductUseCase extends UseCase<ProductDetailData> {
    private ProductRepository productRepository;

    @Inject
    public GetProductUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ProductCloudRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductDetailData> createObservable(RequestParams requestParams) {
        return productRepository.getProduct(requestParams);
    }
}
