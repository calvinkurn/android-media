package com.tokopedia.chat_common.di

import com.tokopedia.chat_common.view.fragment.TopChatRoomFragment
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatScope
@Component(
        modules = arrayOf(ChatModule::class),
        dependencies = arrayOf(ChatRoomComponent::class)
)

interface ChatComponent{
    fun inject(fragment: TopChatRoomFragment)
}