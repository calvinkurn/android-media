package com.tokopedia.topchat.chatlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatlist.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.fragment.ChatListInboxFragment
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment
import dagger.Component

/**
 * @author : Steven 2019-08-07
 */

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

interface ChatListComponent {
    fun inject(fragment: ChatListFragment)
    fun inject(activity: ChatListActivity)
    fun inject(chatTabListFragment: ChatTabListFragment)
    fun inject(chatListInbox: ChatListInboxFragment)
}