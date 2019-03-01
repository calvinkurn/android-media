package com.tokopedia.groupchat.room.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.groupchat.chatroom.data.ChatroomApi
import com.tokopedia.groupchat.common.data.GroupChatUrl
import com.tokopedia.groupchat.common.di.qualifier.GcpQualifier
import com.tokopedia.groupchat.common.di.qualifier.GroupChatQualifier
import com.tokopedia.groupchat.common.network.PlayInterceptor
import com.tokopedia.groupchat.common.network.StreamErrorResponse
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by nisie on 12/12/18.
 */
@PlayScope
@Module
class PlayModule {

    @PlayScope
    @Provides
    fun provideAnalyticTracker(@ApplicationContext context : Context): AnalyticTracker {
        return (context as AbstractionRouter).analyticTracker
    }

    @PlayScope
    @Provides
    @GroupChatQualifier
    fun provideChatroomRetrofit(retrofitBuilder: Retrofit.Builder,
                                okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build()
    }

    @PlayScope
    @Provides
    fun provideChatroomApi(@GroupChatQualifier retrofit: Retrofit): ChatroomApi {
        return retrofit.create(ChatroomApi::class.java)
    }

    @PlayScope
    @Provides
    @GcpQualifier
    fun provideChatroomGCPRetrofit(retrofitBuilder: Retrofit.Builder,
                                okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_GCP_URL).client(okHttpClient).build()
    }

    @PlayScope
    @Provides
    @GcpQualifier
    fun provideChatroomGCPApi(@GcpQualifier retrofit: Retrofit): ChatroomApi {
        return retrofit.create(ChatroomApi::class.java)
    }


    @PlayScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @PlayScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @PlayScope
    @Provides
    fun provideAccountsAuthorizationInterceptor(userSession: com.tokopedia.abstraction.common.data.model.session.UserSession): AccountsAuthorizationInterceptor {
        return AccountsAuthorizationInterceptor(userSession)
    }

    @PlayScope
    @Provides
    fun provideOkHttpClient(@ApplicationContext context : Context,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            playInterceptor: PlayInterceptor):
            OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(ErrorResponseInterceptor(StreamErrorResponse::class.java))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(playInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools()))
                    .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }
}