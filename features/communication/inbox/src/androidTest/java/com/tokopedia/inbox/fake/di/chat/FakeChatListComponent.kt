package com.tokopedia.inbox.fake.di.chat

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.fake.InboxChatFakeDependency
import com.tokopedia.topchat.chatlist.di.*
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    modules = [
        FakeChatListNetworkModule::class,
        ChatListViewsModelModule::class,
        ChatListModule::class,
        FakeChatListUseCase::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FakeChatListComponent : ChatListComponent {
    fun injectMembers(inboxChatFakeDependency: InboxChatFakeDependency)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun baseComponent(component: BaseAppComponent): Builder
        fun build(): FakeChatListComponent
    }
}
