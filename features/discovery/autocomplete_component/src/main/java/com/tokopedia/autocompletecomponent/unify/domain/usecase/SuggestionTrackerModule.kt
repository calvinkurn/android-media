package com.tokopedia.autocompletecomponent.unify.domain.usecase

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.autocompletecomponent.di.AutoCompleteScope
import com.tokopedia.autocompletecomponent.network.AutocompleteBaseURL
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionApi
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionDataSource
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionRepositoryImpl
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionNoAuth
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionQualifier
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
class SuggestionTrackerModule {
    @AutoCompleteScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }

    @AutoCompleteScope
    @Provides
    internal fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context,
            GlobalConfig.isAllowDebuggingTools()
        )

        return ChuckerInterceptor(
            context,
            collector
        )
    }

    @AutoCompleteScope
    @Provides
    fun provideTkpdBaseInterceptor(): TkpdBaseInterceptor {
        return TkpdBaseInterceptor()
    }

    @AutoCompleteScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @AutoCompleteScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @AutoCompleteScope
    @SuggestionNoAuth
    @Provides
    fun provideOkHttpClientNoAuth(
        okHttpRetryPolicy: OkHttpRetryPolicy,
        chuckInterceptor: ChuckerInterceptor,
        debugInterceptor: DebugInterceptor,
        tkpdAuthInterceptor: TkpdAuthInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(tkpdAuthInterceptor)
            .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(debugInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }
        return clientBuilder.build()
    }

    @AutoCompleteScope
    @Provides
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @AutoCompleteScope
    @SuggestionQualifier
    @Provides
    internal fun provideSuggestionApi(
        builder: Retrofit.Builder,
        @SuggestionNoAuth okHttpClient: OkHttpClient
    ): SuggestionApi {
        return builder.baseUrl(AutocompleteBaseURL.Ace.ACE_DOMAIN)
            .client(okHttpClient)
            .build()
            .create<SuggestionApi>(SuggestionApi::class.java)
    }

    @AutoCompleteScope
    @Provides
    internal fun provideSuggestionRepository(
        @SuggestionQualifier suggestionApi: SuggestionApi
    ): SuggestionRepository {
        return SuggestionRepositoryImpl(
            SuggestionDataSource(suggestionApi)
        )
    }
}
