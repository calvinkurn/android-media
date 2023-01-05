package com.tokopedia.kol.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kol.common.data.source.KolAuthInterceptor
import com.tokopedia.kol.common.data.source.api.KolApi
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kol.feature.video.view.presenter.VideoDetailPresenter
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author by milhamj on 06/02/18.
 */
@Module(includes = [FeedComponentModule::class])
class KolModule {
    @KolScope
    @Provides
    fun provideVideoDetailPresenter(presenter: VideoDetailPresenter): VideoDetailContract.Presenter {
        return presenter
    }

    @KolScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context?,
        userSession: UserSessionInterface?,
        networkRouter: NetworkRouter?
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @KolScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @KolScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @KolScope
    @Provides
    fun provideOkHttpClient(
        @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
        kolAuthInterceptor: KolAuthInterceptor,
        @KolQualifier retryPolicy: OkHttpRetryPolicy,
        @KolChuckQualifier chuckInterceptor: Interceptor,
        @ApplicationContext context: Context?
    ): OkHttpClient {
        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
            .addInterceptor(kolAuthInterceptor)
            .addInterceptor(FingerprintInterceptor(context as NetworkRouter?,
                UserSession(context)))
        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }
        return clientBuilder.build()
    }

    @KolScope
    @Provides
    @KolQualifier
    fun provideKolRetrofit(
        okHttpClient: OkHttpClient?,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(TokopediaUrl.getInstance().GQL).client(okHttpClient).build()
    }

    @KolScope
    @Provides
    fun provideKolApi(@KolQualifier retrofit: Retrofit): KolApi {
        return retrofit.create(KolApi::class.java)
    }

    @KolScope
    @KolQualifier
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY)
    }

    @KolScope
    @Provides
    @KolChuckQualifier
    fun provideChuckInterceptory(@ApplicationContext context: Context): Interceptor {
        return ChuckerInterceptor(context)
    }

    @KolScope
    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Main
    }

    @KolScope
    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @KolScope
    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    @KolScope
    @Provides
    fun providesFeedAnalyticTracker(
        trackingQueue: TrackingQueue?,
        userSessionInterface: UserSessionInterface?
    ): FeedAnalyticTracker {
        return FeedAnalyticTracker(trackingQueue!!, userSessionInterface!!)
    }

    companion object {
        private const val NET_READ_TIMEOUT = 60
        private const val NET_WRITE_TIMEOUT = 60
        private const val NET_CONNECT_TIMEOUT = 60
        private const val NET_RETRY = 1
    }
}
