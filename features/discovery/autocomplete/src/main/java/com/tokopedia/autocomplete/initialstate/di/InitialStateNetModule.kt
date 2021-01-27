package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.data.InitialStateApi
import com.tokopedia.autocomplete.network.AutocompleteBaseURL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [InitialStateOkHttpClientModule::class])
class InitialStateNetModule {
    @InitialStateScope
    @InitialStateQualifier
    @Provides
    internal fun provideInitialStateApi(builder: Retrofit.Builder,
                                              @InitialStateNoAuth okHttpClient: OkHttpClient): InitialStateApi {
        return builder.baseUrl(AutocompleteBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create<InitialStateApi>(InitialStateApi::class.java)
    }
}