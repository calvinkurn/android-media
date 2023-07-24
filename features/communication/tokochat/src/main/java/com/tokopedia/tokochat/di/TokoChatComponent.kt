package com.tokopedia.tokochat.di

import com.tokopedia.tokochat.config.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.view.chatlist.TokoChatListActivity
import com.tokopedia.tokochat.view.chatroom.TokoChatActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TokoChatModule::class,
        TokoChatUseCaseModule::class,
        TokoChatViewModelModule::class,
        TokoChatImageAttachmentNetworkModule::class,
        TokoChatFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class]
)
interface TokoChatComponent {
    fun inject(activity: TokoChatActivity)
    fun inject(activity: TokoChatListActivity)
}
