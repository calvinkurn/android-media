package com.tokopedia.posapp.cache.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.product.ProductRepository;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.productlist.domain.model.ProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/29/17.
 */

public class StoreProductCacheUseCase extends UseCaseWithParams<ProductListDomain, DataStatus> {
    ProductRepository productRepository;

    public StoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ProductRepository productRepository) {
        super(threadExecutor, postExecutionThread);
        this.productRepository = productRepository;
    }

    @Override
    public Observable<DataStatus> createObservable(ProductListDomain productListDomain) {
        return productRepository.storeProductsToCache(productListDomain);
    }
}
