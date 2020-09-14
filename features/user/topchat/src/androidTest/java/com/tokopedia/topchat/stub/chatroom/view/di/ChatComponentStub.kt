package com.tokopedia.topchat.stub.chatroom.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.ChatRoomViewModelModule
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.service.NotificationChatService
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import dagger.Component

@ChatScope
@Component(
        modules = [ChatModuleStub::class, ChatRoomContextModule::class, ChatRoomViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ChatComponentStub {
    fun inject(fragment: TopChatRoomFragment)
    fun inject(fragment: StickerFragment)
    fun inject(service: NotificationChatService)
}