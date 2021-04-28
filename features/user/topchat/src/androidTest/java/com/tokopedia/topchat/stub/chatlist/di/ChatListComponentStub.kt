package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.topchat.chatlist.di.*
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListSettingModuleStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.Component

@ChatListScope
@Component(
        modules = [CommonTopchatModule::class,
            ChatListNetworkModuleStub::class,
            ChatListSettingModuleStub::class,
            ChatListQueryModuleStub::class,
            ChatNotificationsQueryModule::class,
            ChatListViewsModelModule::class,
            ChatNotificationsViewsModelModule::class,
            ChatListContextModule::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface ChatListComponentStub : ChatListComponent {

}