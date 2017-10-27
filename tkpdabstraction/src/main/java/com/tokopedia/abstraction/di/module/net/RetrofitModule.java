package com.tokopedia.abstraction.di.module.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.di.scope.ApplicationScope;
import com.tokopedia.abstraction.retrofit.converters.StringResponseConverter;
import com.tokopedia.abstraction.retrofit.converters.TkpdResponseConverter;
import com.tokopedia.abstraction.retrofit.converters.GeneratedHostConverter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @ApplicationScope
    @Provides
    public Retrofit.Builder provideRetrofitBuilder(GeneratedHostConverter generatedHostConverter,
                                                   TkpdResponseConverter tkpdResponseConverter,
                                                   StringResponseConverter stringResponseConverter,
                                                   Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(generatedHostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    @ApplicationScope
    @Provides
    public GeneratedHostConverter provideGeneratedHostConverter() {
        return new GeneratedHostConverter();
    }

    @ApplicationScope
    @Provides
    public TkpdResponseConverter provideTkpdResponseConverter() {
        return new TkpdResponseConverter();
    }

    @ApplicationScope
    @Provides
    public StringResponseConverter provideStringResponseConverter() {
        return new StringResponseConverter();
    }

    @ApplicationScope
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}