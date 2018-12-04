package com.tokopedia.chat_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.chat_common.data.api.ChatRoomApi
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@ChatRoomScope
@Component(
        modules = arrayOf(ChatRoomModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface ChatRoomComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getChatRoomApi(): ChatRoomApi
}