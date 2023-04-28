package com.tokopedia.inbox.fake.di.chat

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.fake.InboxChatFakeDependency
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.topchat.chatlist.di.*
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    modules = [
        FakeChatListNetworkModule::class,
        ChatListViewsModelModule::class,
        ChatListModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface FakeChatListComponent : ChatListComponent {
    fun injectMembers(inboxChatFakeDependency: InboxChatFakeDependency)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun baseComponent(component: FakeBaseAppComponent): Builder
        fun build(): FakeChatListComponent
    }
}
