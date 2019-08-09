package com.tokopedia.topchat.chatlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatroom.di.ChatScope
import dagger.Component

/**
 * @author : Steven 2019-08-07
 */

@ChatListScope
@Component(
        modules = [ChatListNetworkModule::class,
                    ChatListSettingModule::class,
                    ChatListQueryModule::class,
                    ChatListViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)

interface ChatListComponent {
    fun inject (fragment: ChatListFragment)
}