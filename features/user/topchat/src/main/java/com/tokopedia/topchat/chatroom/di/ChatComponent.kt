package com.tokopedia.topchat.chatroom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatroom.service.NotificationChatService
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomSettingsFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatScope
@Component(
        modules = arrayOf(ChatModule::class, ChatRoomContextModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface ChatComponent {
    fun inject(fragment: TopChatRoomFragment)

    fun inject(fragment: ChatRoomSettingsFragment)

    fun inject(service: NotificationChatService)
}