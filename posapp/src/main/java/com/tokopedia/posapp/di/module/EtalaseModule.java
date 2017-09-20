package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.data.factory.ShopFactory;
import com.tokopedia.posapp.data.repository.EtalaseRepository;
import com.tokopedia.posapp.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.di.scope.ShopScope;
import com.tokopedia.posapp.domain.usecase.GetEtalaseUseCase;
import com.tokopedia.posapp.domain.usecase.StoreEtalaseCacheUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/19/17.
 */
// TODO: 9/20/17 fix scope structure
@Module(includes = ShopModule.class)
public class EtalaseModule {

    @ShopScope
    @Provides
    EtalaseRepository provideEtalaseRepository(ShopFactory shopFactory) {
        return new EtalaseRepositoryImpl(shopFactory);
    }

    @ShopScope
    @Provides
    GetEtalaseUseCase provideGetEtalaseUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               EtalaseRepository etalaseRepository) {
        return new GetEtalaseUseCase(threadExecutor, postExecutionThread, etalaseRepository);
    }

    @ShopScope
    @Provides
    StoreEtalaseCacheUseCase provideStoreEtalaseCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             EtalaseRepository etalaseRepository) {
        return new StoreEtalaseCacheUseCase(threadExecutor, postExecutionThread, etalaseRepository);
    }
}
