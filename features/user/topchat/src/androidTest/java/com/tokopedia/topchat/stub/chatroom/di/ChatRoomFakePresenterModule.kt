package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakePresenterModule {

    @ChatScope
    @Provides
    fun provideRemoteConfig(@TopchatContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

}