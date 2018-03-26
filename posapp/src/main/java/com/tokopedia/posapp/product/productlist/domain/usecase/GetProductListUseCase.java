package com.tokopedia.posapp.product.productlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.product.common.data.repository.ProductCloudRepository;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/28/17.
 */

public class GetProductListUseCase extends UseCase<ProductListDomain> {
    private ProductRepository productRepository;

    @Inject
    public GetProductListUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ProductCloudRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<ProductListDomain> createObservable(RequestParams requestParams) {
        return productRepository.getProductList(requestParams);
    }
}
