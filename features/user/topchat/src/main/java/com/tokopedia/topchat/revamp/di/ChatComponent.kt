package com.tokopedia.topchat.revamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.revamp.di.ChatModule
import com.tokopedia.topchat.revamp.view.TopChatRoomFragment
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatScope
@Component(
        modules = arrayOf(ChatModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)

interface ChatComponent{
    fun inject(fragment: TopChatRoomFragment)
}