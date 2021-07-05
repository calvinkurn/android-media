package com.tokopedia.search.di.module

import com.google.gson.Gson
import com.tokopedia.search.di.qualifier.SearchQualifier
import com.tokopedia.search.di.scope.SearchScope
import dagger.Provides
import retrofit2.Retrofit
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.search.result.network.converterfactory.GeneratedHostConverter
import dagger.Module
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

@Module
class SearchRetrofitBuilderModule {
    @SearchQualifier
    @SearchScope
    @Provides
    fun provideTopAdsRetrofitBuilder(gson: Gson?): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GeneratedHostConverter())
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }
}