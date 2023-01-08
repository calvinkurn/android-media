package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.di.*
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListUseCaseModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListSettingModuleStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ChatListScope
@Component(
        modules = [CommonTopchatModule::class,
            ChatListNetworkModuleStub::class,
            ChatListSettingModuleStub::class,
            ChatListUseCaseModuleStub::class,
            ChatNotificationsUseCaseModule::class,
            ChatListViewsModelModule::class,
            ChatNotificationsViewsModelModule::class,
            ChatListContextModule::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface ChatListComponentStub : ChatListComponent {
    fun inject(chatListTest: ChatListTest)
}