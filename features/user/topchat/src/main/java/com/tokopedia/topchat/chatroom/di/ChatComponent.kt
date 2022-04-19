package com.tokopedia.topchat.chatroom.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topchat.chatroom.service.NotificationChatService
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.fragment.StickerFragment
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatScope
@Component(
        modules = [ChatModule::class, ChatRoomContextModule::class, ChatRoomViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface ChatComponent {
    fun inject(fragment: TopChatRoomFragment)

    fun inject(fragment: StickerFragment)

    fun inject(service: NotificationChatService)

    fun inject(service: UploadImageChatService)
}