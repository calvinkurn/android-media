package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.stub.chatroom.view.viewmodel.TopChatRoomViewModelStub
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakeModule {

    @ChatScope
    @Provides
    fun provideRemoteConfig(@TopchatContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ChatScope
    @Provides
    fun provideTopChatViewModel(
        topChatRoomViewModelStub: TopChatRoomViewModelStub
    ): TopChatViewModel {
        return topChatRoomViewModelStub
    }

}