package com.tokopedia.topchat.stub.chatlist.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iris.util.Session
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.stub.fake.FakeIrisSession
import dagger.Module
import dagger.Provides

@Module
class ChatlistCommonModule {

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

    @ChatListScope
    @Provides
    fun provideFakeIrisSession(): Session {
        return FakeIrisSession()
    }
}
