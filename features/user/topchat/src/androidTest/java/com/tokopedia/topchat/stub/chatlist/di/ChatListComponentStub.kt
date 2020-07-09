package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatlist.di.*
import dagger.Component

@ChatListScope
@Component(
        modules = [CommonTopchatModule::class,
            ChatListNetworkModule::class,
            ChatListSettingModule::class,
            ChatListQueryModule::class,
            ChatNotificationsQueryModule::class,
            ChatListViewsModelModule::class,
            ChatNotificationsViewsModelModule::class,
            ChatListContextModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface ChatListComponentStub: ChatListComponent {

}