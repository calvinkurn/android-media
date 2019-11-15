package com.tokopedia.topchat.chatsetting.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatsetting.view.fragment.ChatSettingFragment
import dagger.Component

@ChatSettingScope
@Component(
        modules = [ChatSettingModule::class, ChatSettingViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)

interface ChatSettingComponent {
    fun inject(chatSettingFragment: ChatSettingFragment)
}