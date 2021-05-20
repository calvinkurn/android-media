package com.tokopedia.topchat.chatlist.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatlist.viewmodel.TopChatWebSocket
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
class CommonTopchatModule {

    @Provides
    @ChatListScope
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ChatListScope
    @Provides
    internal fun provideTopchatSharedPrefs(@TopchatContext context: Context): SharedPreferences {
        return context.getSharedPreferences("topchat_prefs", Context.MODE_PRIVATE)
    }

    @ChatListScope
    @Provides
    fun provideTopChatWebSocket(
            userSession: UserSessionInterface,
            tkpdAuthInterceptor: TkpdAuthInterceptor,
            fingerprintInterceptor: FingerprintInterceptor
    ): TopChatWebSocket {
        val webSocketUrl = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + userSession.deviceId +
                "&user_id=" + userSession.userId

        val client = OkHttpClient()
        client.run {
            newBuilder()
                    .addInterceptor(tkpdAuthInterceptor)
                    .addInterceptor(fingerprintInterceptor)
        }

        return TopChatWebSocket(client, webSocketUrl, userSession.accessToken)
    }
}
