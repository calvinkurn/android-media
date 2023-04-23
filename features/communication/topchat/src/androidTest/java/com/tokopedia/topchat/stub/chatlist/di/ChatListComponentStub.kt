package com.tokopedia.topchat.stub.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.di.*
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListSettingModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListUseCaseModuleStub
import com.tokopedia.topchat.stub.common.di.FakeBaseAppComponent
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ChatListModuleStub::class,
        ChatListNetworkModuleStub::class,
        ChatListSettingModuleStub::class,
        ChatListUseCaseModuleStub::class,
        ChatNotificationsUseCaseModule::class,
        ChatListViewsModelModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface ChatListComponentStub : ChatListComponent {
    fun inject(chatListTest: ChatListTest)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): ChatListComponentStub.Builder
        fun baseComponent(component: FakeBaseAppComponent): Builder
        fun build(): ChatListComponentStub
    }
}
