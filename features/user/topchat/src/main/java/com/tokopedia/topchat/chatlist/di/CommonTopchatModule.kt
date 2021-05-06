package com.tokopedia.topchat.chatlist.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides

@Module
class CommonTopchatModule {

    @ChatListScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ChatListScope
    @Provides
    internal fun provideTopchatSharedPrefs(@TopchatContext context: Context): SharedPreferences {
        return context.getSharedPreferences("topchat_prefs", Context.MODE_PRIVATE)
    }

}
