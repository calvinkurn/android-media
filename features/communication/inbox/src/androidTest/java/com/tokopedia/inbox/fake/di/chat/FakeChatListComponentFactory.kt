package com.tokopedia.inbox.fake.di.chat

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.di.ChatListComponent

class FakeChatListComponentFactory : ActivityComponentFactory() {
    private val baseComponent: BaseAppComponent = DaggerBaseAppComponent.builder()
        .appModule(AppModule(ApplicationProvider.getApplicationContext()))
        .build()
    val chatListComponent: FakeChatListComponent = DaggerFakeChatListComponent.builder().baseComponent(baseComponent)
        .context(ApplicationProvider.getApplicationContext())
        .build()

    override fun createChatListComponent(
        application: Application,
        context: Context
    ): ChatListComponent {
        return chatListComponent
    }
}
