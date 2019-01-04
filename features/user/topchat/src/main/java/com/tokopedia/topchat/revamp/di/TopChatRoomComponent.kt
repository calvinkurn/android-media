package com.tokopedia.topchat.revamp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.topchat.revamp.data.api.ChatRoomApi
import dagger.Component

/**
 * @author : Steven 29/11/18
 */

@TopChatRoomScope
@Component(
        modules = arrayOf(TopChatRoomModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface TopChatRoomComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getChatRoomApi(): ChatRoomApi
}