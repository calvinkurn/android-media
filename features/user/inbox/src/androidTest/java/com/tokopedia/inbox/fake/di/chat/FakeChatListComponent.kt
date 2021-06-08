package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.inbox.view.activity.base.chat.InboxChatTest
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
    fun inject(inboxChatTest: InboxChatTest)
}