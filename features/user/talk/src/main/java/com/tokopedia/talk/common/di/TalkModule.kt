package com.tokopedia.talk.common.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.talk.addtalk.domain.mapper.CreateTalkMapper
import com.tokopedia.talk.addtalk.domain.usecase.CreateTalkUsecase
import com.tokopedia.talk.common.data.TalkApi
import com.tokopedia.talk.common.data.TalkUrl
import com.tokopedia.talk.common.domain.mapper.BaseActionMapper
import com.tokopedia.talk.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk.common.domain.usecase.DeleteTalkUseCase
import com.tokopedia.talk.common.network.TalkErrorResponse
import com.tokopedia.talk.inboxtalk.domain.GetInboxTalkUseCase
import com.tokopedia.talk.inboxtalk.domain.mapper.GetInboxTalkMapper
import com.tokopedia.talk.producttalk.domain.mapper.ProductTalkListMapper
import com.tokopedia.talk.producttalk.domain.usecase.GetProductTalkUseCase
import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by nisie on 8/29/18.
 */
@TalkScope
@Module
class TalkModule {

    @TalkScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TalkScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @TalkScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @TalkScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @TalkScope
    @Provides
    fun provideOkHttpClient(chuckInterceptor: ChuckInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            debugInterceptor: DebugInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
        builder.addInterceptor(ErrorResponseInterceptor(TalkErrorResponse::class.java))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(debugInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @TalkScope
    @Provides
    fun provideTalkRetrofit(retrofitBuilder: Retrofit.Builder,
                            @TalkScope okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(TalkUrl.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @TalkScope
    @Provides
    fun provideTalkApi(@TalkScope retrofit: Retrofit): TalkApi {
        return retrofit.create(TalkApi::class.java)
    }

    @TalkScope
    @Provides
    fun provideDetailTalkApi(@TalkScope retrofit: Retrofit): DetailTalkApi {
        return retrofit.create(DetailTalkApi::class.java)
    }

    @TalkScope
    @Provides
    fun provideGetInboxTalkUseCase(api: TalkApi,
                                   getInboxTalkMapper: GetInboxTalkMapper): GetInboxTalkUseCase {
        return GetInboxTalkUseCase(api, getInboxTalkMapper)
    }

    @TalkScope
    @Provides
    fun provideGetProductTalkUseCase(api: TalkApi,
                                     getProductTalkMapper: ProductTalkListMapper): GetProductTalkUseCase {
        return GetProductTalkUseCase(api, getProductTalkMapper)
    }

    @TalkScope
    @Provides
    fun provideDeleteTalkUseCase(api: TalkApi,
                                 baseActionMapper: BaseActionMapper): DeleteTalkUseCase {
        return DeleteTalkUseCase(api, baseActionMapper)
    }

    @TalkScope
    @Provides
    fun provideDeleteCommentTalkUseCase(api: TalkApi,
                                        baseActionMapper: BaseActionMapper): DeleteCommentTalkUseCase {
        return DeleteCommentTalkUseCase(api, baseActionMapper)
    }

    @TalkScope
    @Provides
    fun provideCreateTalkUseCase(api: TalkApi,
                                 createTalkMapper: CreateTalkMapper): CreateTalkUsecase {
        return CreateTalkUsecase(api, createTalkMapper)
    }

    @TalkScope
    @Provides
    fun provideAnalyticTracker(abstractionRouter: AbstractionRouter): AnalyticTracker {
        return abstractionRouter.analyticTracker
    }
}