package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.repository.ShopRepository;
import com.tokopedia.posapp.di.scope.PosCacheScope;
import com.tokopedia.posapp.di.scope.ShopScope;
import com.tokopedia.posapp.domain.usecase.GetShopProductListUseCase;
import com.tokopedia.posapp.domain.usecase.StoreProductCacheUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/5/17.
 */

@Module(includes = ShopModule.class)
public class ShopProductModule {

    @Provides
    @ShopScope
    GetShopProductListUseCase provideGetProductListUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           ShopRepository shopRepository) {
        return new GetShopProductListUseCase(threadExecutor, postExecutionThread, shopRepository);
    }

    @Provides
    @ShopScope
    StoreProductCacheUseCase provideStoreProductCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ShopRepository shopRepository) {
        return new StoreProductCacheUseCase(threadExecutor, postExecutionThread, shopRepository);
    }
}
