package com.tokopedia.favorite.di.modul;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.favorite.data.source.apis.converterfactory.GeneratedHostConverter;
import com.tokopedia.favorite.di.qualifier.TopAdsQualifier;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.network.CoroutineCallAdapterFactory;
import com.tokopedia.network.converter.StringResponseConverter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@FavoriteScope
@Module
public class FavoriteRetrofitBuilderModule {

    @TopAdsQualifier
    @FavoriteScope
    @Provides
    public Retrofit.Builder provideTopAdsRetrofitBuilder(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.create());
    }
}
