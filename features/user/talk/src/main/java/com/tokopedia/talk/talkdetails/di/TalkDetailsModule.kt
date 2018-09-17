package com.tokopedia.talk.talkdetails.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.talk.common.data.TalkUrl
import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.talk.talkdetails.domain.mapper.DeleteTalkCommentsMapper
import com.tokopedia.talk.talkdetails.domain.mapper.GetTalkCommentsMapper
import com.tokopedia.talk.talkdetails.domain.usecase.DeleteTalkCommentsUseCase
import com.tokopedia.talk.talkdetails.domain.usecase.GetTalkCommentsUseCase
import com.tokopedia.talk.talkdetails.view.presenter.TalkDetailsPresenter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Hendri on 03/09/18.
 */

@TalkDetailsScope
@Module
class TalkDetailsModule {

    @Provides
    @TalkDetailsScope
    fun provideTalkDetailsPresenter(getTalkCommentsUseCase: GetTalkCommentsUseCase,
                                    deleteTalkCommentsUseCase: DeleteTalkCommentsUseCase): TalkDetailsPresenter {
        return TalkDetailsPresenter(getTalkCommentsUseCase, deleteTalkCommentsUseCase)
    }

    @Provides
    @TalkDetailsScope
    fun provideGetTalkCommentsUseCase(detailsTalkApi: DetailTalkApi,
                                      getTalkCommentsMapper: GetTalkCommentsMapper)
            : GetTalkCommentsUseCase {
        return GetTalkCommentsUseCase(detailsTalkApi, getTalkCommentsMapper)
    }

    @Provides
    @TalkDetailsScope
    fun provideGetTalkCommentsMapper(): GetTalkCommentsMapper {
        return GetTalkCommentsMapper()
    }

    @Provides
    @TalkDetailsScope
    fun provideDeleteTalkCommentsUseCase(detailsTalkApi: DetailTalkApi,
                                         deleteTalkCommentsMapper: DeleteTalkCommentsMapper)
            : DeleteTalkCommentsUseCase {
        return DeleteTalkCommentsUseCase(detailsTalkApi, deleteTalkCommentsMapper)
    }

    @Provides
    @TalkDetailsScope
    fun provideDeleteTalkCommentsMapper(): DeleteTalkCommentsMapper {
        return DeleteTalkCommentsMapper()
    }

    @Provides
    @TalkDetailsScope
    fun provideDetailsTalkApi(retrofit: Retrofit): DetailTalkApi {
        return retrofit.create(DetailTalkApi::class.java)
    }

    @Provides
    @TalkDetailsScope
    fun provideHttpClient(@ApplicationContext context: Context,
                          userSession: UserSession,
                          chuckInterceptor: ChuckInterceptor): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        val networkRouter: NetworkRouter = context as NetworkRouter
        val fingerprintInterceptor = FingerprintInterceptor(networkRouter, userSession)
        val tkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)
        val headerResponseInterceptor =
                HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)

        builder.addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(headerResponseInterceptor)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(DebugInterceptor())
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @TalkDetailsScope
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls().create()
    }

    @Provides
    @TalkDetailsScope
    fun provideStringResponseConverter(): StringResponseConverter {
        return StringResponseConverter()
    }

    @Provides
    @TalkDetailsScope
    fun provideRetrofit(gson: Gson,
                        stringResponseConverter: StringResponseConverter,
                        okHttpClient: OkHttpClient): Retrofit {
        val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
        return retrofitBuilder
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(TalkUrl.BASE_URL)
                .client(okHttpClient)
                .build()
    }


    @Provides
    @TalkDetailsScope
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }
}