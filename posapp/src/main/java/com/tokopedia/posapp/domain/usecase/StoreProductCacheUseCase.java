package com.tokopedia.posapp.domain.usecase;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.base.domain.UseCaseWithParams;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.database.ProductSavedResult;
import com.tokopedia.posapp.domain.model.shop.ShopDomain;
import com.tokopedia.posapp.domain.model.shop.ShopProductListDomain;

import rx.Observable;

/**
 * Created by okasurya on 8/29/17.
 */

public class StoreProductCacheUseCase extends UseCaseWithParams<ShopProductListDomain, ProductSavedResult> {
    ShopRepository shopRepository;

    public StoreProductCacheUseCase(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          ShopRepository shopRepository) {
        super(threadExecutor, postExecutionThread);
        this.shopRepository = shopRepository;
    }

    @Override
    public Observable<ProductSavedResult> createObservable(ShopProductListDomain shopProductListDomain) {
        return shopRepository.storeProductToCache(shopProductListDomain);
    }
}
