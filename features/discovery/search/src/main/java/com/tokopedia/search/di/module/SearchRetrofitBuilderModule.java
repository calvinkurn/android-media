package com.tokopedia.search.di.module;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.network.converter.StringResponseConverter;
import com.tokopedia.search.di.qualifier.SearchQualifier;
import com.tokopedia.search.result.network.converterfactory.GeneratedHostConverter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@SearchScope
@Module
public class SearchRetrofitBuilderModule {

    @SearchQualifier
    @SearchScope
    @Provides
    public Retrofit.Builder provideTopAdsRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
