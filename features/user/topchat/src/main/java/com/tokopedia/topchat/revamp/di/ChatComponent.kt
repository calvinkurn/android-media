package com.tokopedia.topchat.revamp.di

import com.tokopedia.topchat.revamp.view.TopChatRoomFragment
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatScope
@Component(
        modules = arrayOf(ChatModule::class),
        dependencies = arrayOf(TopChatRoomComponent::class)
)

interface ChatComponent{
    fun inject(fragment: TopChatRoomFragment)
}