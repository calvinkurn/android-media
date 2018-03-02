package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.posapp.cache.data.factory.EtalaseFactory;
import com.tokopedia.posapp.cache.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepository;
import com.tokopedia.posapp.cache.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.product.productlist.data.source.cloud.ProductListApi;
import com.tokopedia.posapp.di.scope.EtalaseScope;
import com.tokopedia.posapp.product.productlist.domain.usecase.GetEtalaseCacheUseCase;
import com.tokopedia.posapp.cache.domain.usecase.GetEtalaseUseCase;
import com.tokopedia.posapp.cache.domain.usecase.StoreEtalaseCacheUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 9/19/17.
 */

@EtalaseScope
@Module
public class EtalaseModule {
    @Provides
    GetEtalaseMapper provideGetEtalaseMapper(Gson gson) {
        return new GetEtalaseMapper(gson);
    }

    @Provides
    EtalaseFactory provideEtalaseFactory(ProductListApi posProductApi, GetEtalaseMapper getEtalaseMapper) {
        return new EtalaseFactory(posProductApi, getEtalaseMapper);
    }

    @Provides
    EtalaseRepository provideEtalaseRepository(EtalaseFactory etalaseFactory) {
        return new EtalaseRepositoryImpl(etalaseFactory);
    }

    @Provides
    GetEtalaseUseCase provideGetEtalaseUseCase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               EtalaseRepository etalaseRepository) {
        return new GetEtalaseUseCase(threadExecutor, postExecutionThread, etalaseRepository);
    }

    @Provides
    StoreEtalaseCacheUseCase provideStoreEtalaseCacheUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             EtalaseRepository etalaseRepository) {
        return new StoreEtalaseCacheUseCase(threadExecutor, postExecutionThread, etalaseRepository);
    }

    @Provides
    GetEtalaseCacheUseCase provideGetEtalaseCacheUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         EtalaseRepository etalaseRepository) {
        return new GetEtalaseCacheUseCase(threadExecutor, postExecutionThread, etalaseRepository);
    }
}
