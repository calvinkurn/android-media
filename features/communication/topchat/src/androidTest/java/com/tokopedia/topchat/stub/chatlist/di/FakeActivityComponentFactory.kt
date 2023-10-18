package com.tokopedia.topchat.stub.chatlist.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.di.ChatListComponent

class FakeActivityComponentFactory : ActivityComponentFactory() {

    private val baseComponent: BaseAppComponent = DaggerBaseAppComponent.builder()
        .appModule(object : AppModule(ApplicationProvider.getApplicationContext()) {
            override fun provideGraphqlRepository(): GraphqlRepository {
                return FakeGraphqlRepository()
            }
        })
        .build()
    val chatListComponent: ChatListComponentStub = DaggerChatListComponentStub.builder().baseComponent(baseComponent)
        .context(ApplicationProvider.getApplicationContext())
        .build()

    override fun createChatListComponent(
        application: Application,
        context: Context
    ): ChatListComponent {
        return chatListComponent
    }
}
