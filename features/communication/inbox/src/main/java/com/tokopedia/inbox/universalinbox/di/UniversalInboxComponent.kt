package com.tokopedia.inbox.universalinbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.view.UniversalInboxActivity
import com.tokopedia.tokochat.config.di.component.TokoChatConfigComponent
import dagger.Component

@ActivityScope
@Component(
    modules = [
        UniversalInboxModule::class,
        UniversalInboxUseCaseModule::class,
        UniversalInboxTokoChatUseCaseModule::class,
        UniversalInboxViewModelModule::class,
        UniversalInboxFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class]
)
interface UniversalInboxComponent {
    fun inject(activity: UniversalInboxActivity)
}
