package com.tokopedia.attachproduct.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.data.source.url.AttachProductUrl
import com.tokopedia.attachproduct.domain.usecase.NewAttachProductUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by Hendri on 19/02/18.
 */
@Module
class NewAttachProductModule(private val context: Context) {

    @Provides
    @AttachProductContext
    fun provideAttachProductContext(): Context {
        return context;
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @AttachProductScope
    @Provides
    fun provideErrorResponseInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @AttachProductScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @AttachProductScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context?,
                                   networkRouter: NetworkRouter?,
                                   userSession: UserSession?): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @AttachProductScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter?,
                                      userSession: UserSession?): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @AttachProductScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @AttachProductScope
    fun provideQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_query_attach_product)
    }

    @Provides
    @AttachProductScope
    fun provideUseCase(repository: GraphqlRepository, query: String): NewAttachProductUseCase {
        return NewAttachProductUseCase(repository, query, Dispatchers.IO)
    }

    @Provides
    @AttachProductScope
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
