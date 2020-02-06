package com.tokopedia.topchat.chatsearch.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatsearch.view.fragment.ChatSearchFragment
import dagger.Component

@ChatSearchScope
@Component(
        modules = [ChatSearchModule::class, ChatSearchViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ChatSearchComponent {
    fun inject(chatSettingFragment: ChatSearchFragment)
}