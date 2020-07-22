package com.tokopedia.topchat.chatsearch.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
import com.tokopedia.topchat.chatsearch.view.fragment.ContactLoadMoreChatFragment
import dagger.Component

@ChatSearchScope
@Component(
        modules = [ChatSearchModule::class, ChatSearchViewsModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ChatSearchComponent {
    fun inject(chatSettingFragment: ChatSearchFragment)
    fun inject(chatSearchContactDetailFragment: ContactLoadMoreChatFragment)
}