package com.tokopedia.inbox.universalinbox.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.di.UniversalInboxFragmentModule
import com.tokopedia.inbox.universalinbox.di.UniversalInboxUseCaseModule
import com.tokopedia.inbox.universalinbox.di.UniversalInboxViewModelModule
import com.tokopedia.inbox.universalinbox.stub.di.base.UniversalInboxFakeBaseAppComponent
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import dagger.Component

@ActivityScope
@Component(
    modules = [
        // Stub modules
        UniversalInboxModuleStub::class,

        // Real modules
        UniversalInboxUseCaseModule::class,
        UniversalInboxViewModelModule::class,
        UniversalInboxFragmentModule::class
    ],
    dependencies = [UniversalInboxFakeBaseAppComponent::class]
)
interface UniversalInboxComponentStub : UniversalInboxComponent {
    fun inject(base: BaseUniversalInboxTest)
}
