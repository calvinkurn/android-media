package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.posapp.data.repository.ProductRepository;
import com.tokopedia.posapp.domain.model.product.ProductDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/9/17.
 */

public class GetProductUseCase extends UseCase<ProductDetailData> {
    private ProductRepository productRepository;

    public GetProductUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductDetailData> createObservable(RequestParams requestParams) {
        return productRepository.getProduct(requestParams);
    }
}
