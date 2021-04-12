package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.ChatRoomViewModelModule
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.service.NotificationChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub
import dagger.Component

@ChatScope
@Component(
        modules = [
            ChatModuleStub::class,
            ChatRoomContextModule::class,
            ChatRoomViewModelModule::class,
            ChatRoomFakeUseCaseModule::class,
            ChatRoomFakePresenterModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface ChatComponentStub: ChatComponent {
    fun inject(topchatRoomTest: TopchatRoomTest)
    fun inject(service: UploadImageChatServiceStub)
}