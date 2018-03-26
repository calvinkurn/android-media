package com.tokopedia.posapp.outlet.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.posapp.common.PosApiModule;
import com.tokopedia.posapp.outlet.data.source.OutletApi;
import com.tokopedia.posapp.outlet.data.factory.OutletFactory;
import com.tokopedia.posapp.outlet.data.mapper.GetOutletMapper;
import com.tokopedia.posapp.outlet.data.repository.OutletRepository;
import com.tokopedia.posapp.outlet.data.repository.OutletCloudRepository;
import com.tokopedia.posapp.outlet.data.source.OutletCloudSource;
import com.tokopedia.posapp.outlet.domain.usecase.GetOutletUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 7/31/17.
 */

@Module(includes = PosApiModule.class)
public class OutletModule {
    @OutletScope
    @Provides
    OutletApi provideOutletApi(Retrofit retrofit) {
        return retrofit.create(OutletApi.class);
    }

    @OutletScope
    @Provides
    OutletRepository provideOutletCloudRepository(OutletCloudSource outletCloudSource) {
        return new OutletCloudRepository(outletCloudSource);
    }
}
