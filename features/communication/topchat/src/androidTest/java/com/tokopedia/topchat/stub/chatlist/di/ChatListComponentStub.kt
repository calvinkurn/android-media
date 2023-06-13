package com.tokopedia.topchat.stub.chatlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.ChatListViewsModelModule
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListNetworkModuleStub
import com.tokopedia.topchat.stub.chatlist.di.module.ChatListUseCaseModuleStub
import dagger.BindsInstance
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ChatListModuleStub::class,
        ChatListNetworkModuleStub::class,
        ChatListUseCaseModuleStub::class,
        ChatListViewsModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ChatListComponentStub : ChatListComponent {
    fun inject(chatListTest: ChatListTest)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun baseComponent(component: BaseAppComponent): Builder
        fun build(): ChatListComponentStub
    }
}
