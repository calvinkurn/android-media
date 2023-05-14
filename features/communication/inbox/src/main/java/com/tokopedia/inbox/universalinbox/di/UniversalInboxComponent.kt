package com.tokopedia.inbox.universalinbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.view.UniversalInboxFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        UniversalInboxModule::class,
        UniversalInboxViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface UniversalInboxComponent {
    fun inject(fragment: UniversalInboxFragment)
}
