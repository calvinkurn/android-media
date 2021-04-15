package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatlist.di.*
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListDispatcherModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListQueryModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListSettingModuleStub
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
            ChatListContextModule::class,
            ChatListDispatcherModuleStub::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface ChatListComponentStub: ChatListComponent {

}