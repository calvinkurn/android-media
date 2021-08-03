package com.tokopedia.review.feature.reputationhistory.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.ShopInfoCloud
import com.tokopedia.review.feature.reputationhistory.data.source.ShopInfoDataSource
import com.tokopedia.review.feature.reputationhistory.data.repository.ShopInfoRepositoryImpl
import com.tokopedia.network.NetworkRouter
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.ChuckerCollector
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.network.retrofit.interceptors.TkpdErrorResponseInterceptor
import retrofit2.Retrofit
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.apiservice.api.SellerReputationApi
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.CloudReputationReviewDataSource
import com.tokopedia.review.feature.reputationhistory.domain.ReputationReviewRepository
import com.tokopedia.review.feature.reputationhistory.data.repository.ReputationReviewRepositoryImpl
import com.tokopedia.shop.common.constant.ShopCommonUrl
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.core.network.constants.TkpdBaseURL
import com.tokopedia.core.network.retrofit.response.TkpdV4ResponseError
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.review.feature.reputationhistory.data.mapper.SimpleDataResponseMapper
import com.tokopedia.review.feature.reputationhistory.data.repository.ReputationRepositoryImpl
import com.tokopedia.review.feature.reputationhistory.data.source.cloud.apiservice.api.ShopApi
import com.tokopedia.review.feature.reputationhistory.domain.ReputationRepository
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

/**
 * Created by normansyahputa on 2/13/18.
 */
@Module(includes = [SellerReputationViewModelModule::class])
class SellerReputationModule {
    @SellerReputationScope
    @Provides
    fun provideSimpleDataResponseMapper(): SimpleDataResponseMapper<*> {
        return SimpleDataResponseMapper<Any?>()
    }

    @SellerReputationScope
    @Provides
    fun provideShopInfoCloud(
        @ApplicationContext context: Context?,
        api: ShopApi?
    ): ShopInfoCloud {
        return ShopInfoCloud(context, api)
    }

    @SellerReputationScope
    @Provides
    fun provideShopInfoDataSource(
        shopInfoCloud: ShopInfoCloud?,
        simpleDataResponseMapper: SimpleDataResponseMapper<ShopModel>
    ): ShopInfoDataSource {
        return ShopInfoDataSource(shopInfoCloud, simpleDataResponseMapper)
    }

    @SellerReputationScope
    @Provides
    fun provideShopInfoRepository(
        @ApplicationContext context: Context?,
        shopInfoDataSource: ShopInfoDataSource?
    ): ShopInfoRepositoryImpl {
        return ShopInfoRepositoryImpl(context, shopInfoDataSource)
    }

    @SellerReputationScope
    @Provides
    fun provideFingerprintInterceptor(
        networkRouter: NetworkRouter?,
        userSession: UserSession?
    ): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @SellerReputationScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context?,
        networkRouter: NetworkRouter?,
        userSession: UserSession?
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @SellerReputationScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context?): NetworkRouter? {
        return context as NetworkRouter?
    }

    @SellerReputationScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context?): ChuckerInterceptor {
        val collector = ChuckerCollector(context!!, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(context, collector)
    }

    @SellerReputationScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }

    @SellerReputationScope
    @Provides
    fun provideTkpdErrorResponseInterceptor(): TkpdErrorResponseInterceptor {
        return TkpdErrorResponseInterceptor(TkpdV4ResponseError::class.java)
    }

    @SellerReputationScope
    @Provides
    fun provideOkHttpClient(
        fingerprintInterceptor: FingerprintInterceptor,
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        chuckInterceptor: ChuckerInterceptor,
        debugInterceptor: DebugInterceptor,
        errorHandlerInterceptor: TkpdErrorResponseInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(fingerprintInterceptor)
            .addInterceptor(tkpdAuthInterceptor)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(debugInterceptor)
            .addInterceptor(errorHandlerInterceptor)
            .build()
    }

    @SellerReputationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @SellerReputationScope
    @Provides
    fun provideInboxRetrofit(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(TkpdBaseURL.INBOX_DOMAIN).client(okHttpClient).build()
    }

    @Provides
    @SellerReputationScope
    fun provideProductActionApi(retrofit: Retrofit): SellerReputationApi {
        return retrofit.create(SellerReputationApi::class.java)
    }

    @Provides
    @SellerReputationScope
    fun provideReputationRepository(cloudReputationReviewDataSource: CloudReputationReviewDataSource?): ReputationRepository {
        return ReputationRepositoryImpl(cloudReputationReviewDataSource)
    }

    @Provides
    @SellerReputationScope
    fun proReputationReviewRepository(
        cloudReputationReviewDataSource: CloudReputationReviewDataSource?,
        shopInfoRepository: ShopInfoRepositoryImpl?
    ): ReputationReviewRepository {
        return ReputationReviewRepositoryImpl(cloudReputationReviewDataSource, shopInfoRepository)
    }

    @ShopWsQualifier
    @SellerReputationScope
    @Provides
    fun provideWSRetrofit(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder.baseUrl(ShopCommonUrl.BASE_WS_URL).client(okHttpClient).build()
    }

    @SellerReputationScope
    @Provides
    fun provideShopApi(@ShopWsQualifier retrofit: Retrofit): ShopApi {
        return retrofit.create(
            ShopApi::class.java
        )
    }

    @Provides
    @SellerReputationScope
    fun provideGcmHandler(@ApplicationContext context: Context?): GCMHandler {
        return GCMHandler(context)
    }
}