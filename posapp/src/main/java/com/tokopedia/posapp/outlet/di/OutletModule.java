package com.tokopedia.posapp.outlet.di;

import com.tokopedia.posapp.outlet.data.source.OutletApi;

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
    OutletApi provideOutletApi(Retrofit retrofit) {
        return retrofit.create(OutletApi.class);
    }
}
