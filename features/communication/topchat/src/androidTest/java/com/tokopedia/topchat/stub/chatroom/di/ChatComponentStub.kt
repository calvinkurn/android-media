package com.tokopedia.topchat.stub.chatroom.di

import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ChatScope
@Component(
        modules = [
            ChatModuleStub::class,
            ChatRoomContextModule::class,
            ChatRoomFakeUseCaseModule::class,
            ChatRoomFakeModule::class,
            ChatRoomFakeViewModelModule::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface ChatComponentStub : ChatComponent {
    fun inject(topchatRoomTest: TopchatRoomTest)
    fun inject(service: UploadImageChatServiceStub)
}