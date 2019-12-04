package com.tokopedia.play.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.play.data.network.ChannelApi
import com.tokopedia.play.data.network.ChannelMapper
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by jegul on 29/11/19
 */
@Module
class PlayModule {

    @PlayScope
    @Provides
    fun provideTokopediaPlayPlayerInstance(@ApplicationContext ctx: Context): TokopediaPlayManager = TokopediaPlayManager.getInstance(ctx)

    @PlayScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @PlayScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        if (context is NetworkRouter) {
            return context
        }
        throw IllegalStateException("Application must implement NetworkRouter")
    }

    @PlayScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, userSession: UserSession, networkRouter: NetworkRouter): Retrofit {
        return CommonNetwork.createRetrofit(
                context,
                TokopediaUrl.getInstance().CHAT,
                networkRouter,
                userSession)
    }

    @PlayScope
    @Provides
    fun provideChannelApi(retrofit: Retrofit): ChannelApi {
        return retrofit.create(ChannelApi::class.java)
    }

    @PlayScope
    @Provides
    fun provideChannelMapper(): ChannelMapper {
        return ChannelMapper()
    }
}