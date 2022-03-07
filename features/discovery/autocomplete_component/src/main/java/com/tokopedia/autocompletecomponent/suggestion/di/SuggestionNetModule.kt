package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.autocompletecomponent.network.AutocompleteBaseURL
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [SuggestionOkHttpClientModule::class])
class SuggestionNetModule {
    @SuggestionScope
    @SuggestionQualifier
    @Provides
    internal fun provideSuggestionApi(builder: Retrofit.Builder,
                                      @SuggestionNoAuth okHttpClient: OkHttpClient): SuggestionApi {
        return builder.baseUrl(AutocompleteBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create<SuggestionApi>(SuggestionApi::class.java)
    }
}