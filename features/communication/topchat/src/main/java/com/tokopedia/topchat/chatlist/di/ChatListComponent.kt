package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.view.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.view.fragment.ChatListInboxFragment
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment
import dagger.BindsInstance
import dagger.Component

/**
 * @author : Steven 2019-08-07
 */

@ChatListScope
@Component(
    modules = [
        ChatListNetworkModule::class,
        ChatListSettingModule::class,
        ChatListUseCaseModule::class,
        ChatNotificationsUseCaseModule::class,
        ChatListViewsModelModule::class,
        ChatListModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ChatListComponent {
    fun inject(fragment: ChatListFragment)
    fun inject(activity: ChatListActivity)
    fun inject(chatTabListFragment: ChatTabListFragment)
    fun inject(chatListInbox: ChatListInboxFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): ChatListComponent.Builder
        fun baseComponent(component: BaseAppComponent): Builder
        fun build(): ChatListComponent
    }
}
