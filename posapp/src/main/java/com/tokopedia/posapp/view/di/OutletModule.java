package com.tokopedia.posapp.view.di;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.posapp.data.factory.OutletFactory;
import com.tokopedia.posapp.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.data.repository.OutletRepository;
import com.tokopedia.posapp.data.repository.OutletRepositoryImpl;
import com.tokopedia.posapp.domain.usecase.GetOutletUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by okasurya on 7/31/17.
 */

@Module
public class OutletModule {

    @OutletScope
    @Provides
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @OutletScope
    @Provides
    GetOutletMapper provideOutletMapper(Gson gson) {
        return new GetOutletMapper(gson);
    }

    @OutletScope
    @Provides
    OutletFactory provideOutletFactory(PeopleService peopleService, GetOutletMapper getOutletMapper) {
        return new OutletFactory(peopleService, getOutletMapper);
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
