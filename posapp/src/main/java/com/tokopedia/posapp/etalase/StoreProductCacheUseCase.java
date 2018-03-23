package com.tokopedia.posapp.etalase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.di.qualifier.CloudSource;
import com.tokopedia.posapp.di.qualifier.LocalSource;
import com.tokopedia.posapp.product.common.data.repository.ProductRepository;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by okasurya on 8/29/17.
 */

public class StoreProductCacheUseCase extends UseCaseWithParams<ProductListDomain, DataStatus> {
    private ProductRepository productRepository;

    @Inject
    public StoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    @LocalSource ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<DataStatus> createObservable(ProductListDomain productListDomain) {
        return productRepository.store(productListDomain);
    }
}
