package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.outlet.data.source.OutletApi;
import com.tokopedia.posapp.outlet.data.factory.OutletFactory;
import com.tokopedia.posapp.outlet.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.outlet.data.repository.OutletRepository;
import com.tokopedia.posapp.outlet.data.repository.OutletRepositoryImpl;
import com.tokopedia.posapp.outlet.domain.usecase.GetOutletUseCase;
import com.tokopedia.posapp.di.scope.OutletScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 7/31/17.
 */

@Module
public class OutletModule {
    @OutletScope
    @Provides
    OutletApi provideOutletApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(OutletApi.class);
    }

    @OutletScope
    @Provides
    GetOutletMapper provideOutletMapper() {
        return new GetOutletMapper();
    }

    @OutletScope
    @Provides
    OutletFactory provideOutletFactory(OutletApi OutletApi, GetOutletMapper getOutletMapper) {
        return new OutletFactory(OutletApi, getOutletMapper);
    }

    @OutletScope
    @Provides
    OutletRepository provideOutletRepository(OutletFactory outletFactory) {
        return new OutletRepositoryImpl(outletFactory);
    }

    @OutletScope
    @Provides
    GetOutletUseCase provideGetOutletUsecase(ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             OutletRepository outletRepository) {
        return new GetOutletUseCase(threadExecutor, postExecutionThread, outletRepository);
    }
}
