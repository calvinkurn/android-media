package com.tokopedia.inbox.universalinbox.di

import com.tokochat.tokochat_config_common.di.component.TokoChatConfigComponent
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.view.UniversalInboxActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        UniversalInboxModule::class,
        UniversalInboxUseCaseModule::class,
        UniversalInboxViewModelModule::class,
        UniversalInboxFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class, TokoChatConfigComponent::class]
)
interface UniversalInboxComponent {
    fun inject(activity: UniversalInboxActivity)
}
