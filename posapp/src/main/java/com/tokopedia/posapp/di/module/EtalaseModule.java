package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.posapp.data.factory.EtalaseFactory;
import com.tokopedia.posapp.data.mapper.GetEtalaseMapper;
import com.tokopedia.posapp.data.repository.EtalaseRepository;
import com.tokopedia.posapp.data.repository.EtalaseRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.GatewayProductApi;
import com.tokopedia.posapp.di.scope.EtalaseScope;
import com.tokopedia.posapp.domain.usecase.GetEtalaseCacheUseCase;
import com.tokopedia.posapp.domain.usecase.GetEtalaseUseCase;
import com.tokopedia.posapp.domain.usecase.StoreEtalaseCacheUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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
    EtalaseFactory provideEtalaseFactory(GatewayProductApi posProductApi, GetEtalaseMapper getEtalaseMapper) {
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
