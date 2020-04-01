package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.di.net.AutoCompleteOkHttpClientModule
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier
import com.tokopedia.autocomplete.di.qualifier.NoAuth
import com.tokopedia.autocomplete.initialstate.data.InitialStateApi
import com.tokopedia.autocomplete.network.AutocompleteBaseURL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@AutoCompleteScope
@Module(includes = [AutoCompleteOkHttpClientModule::class])
class InitialStateNetModule {
    @AutoCompleteScope
    @AutoCompleteQualifier
    @Provides
    internal fun provideInitialStateApi(builder: Retrofit.Builder,
                                              @NoAuth okHttpClient: OkHttpClient): InitialStateApi {
        return builder.baseUrl(AutocompleteBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create<InitialStateApi>(InitialStateApi::class.java)
    }
}