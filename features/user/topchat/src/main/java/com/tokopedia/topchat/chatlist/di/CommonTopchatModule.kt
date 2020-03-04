package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module
class CommonTopchatModule {
    @Provides
    @ChatListScope
    fun provideRemoteConfig(context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

}