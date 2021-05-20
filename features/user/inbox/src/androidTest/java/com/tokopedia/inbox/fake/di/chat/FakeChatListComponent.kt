package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.topchat.chatlist.di.*
import dagger.Component

@ChatListScope
@Component(
        modules = [CommonTopchatModule::class,
            FakeChatListNetworkModule::class,
            ChatListSettingModule::class,
            ChatListQueryModule::class,
            ChatNotificationsQueryModule::class,
            ChatListViewsModelModule::class,
            ChatNotificationsViewsModelModule::class,
            ChatListContextModule::class,
            FakeChatListUseCase::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface FakeChatListComponent : ChatListComponent {
}