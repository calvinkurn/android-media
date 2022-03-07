package com.tokopedia.updateinactivephone.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.di.module.InactivePhoneViewModelModule
import com.tokopedia.updateinactivephone.stub.di.base.FakeBaseAppComponent
import com.tokopedia.updateinactivephone.stub.di.module.FakeInactivePhoneUseCaseModule
import dagger.Component

@ActivityScope
@Component(modules = [
    InactivePhoneModule::class,
    InactivePhoneViewModelModule::class,
    FakeInactivePhoneUseCaseModule::class
], dependencies = [
    FakeBaseAppComponent::class
])
interface InactivePhoneComponentStub : InactivePhoneComponent {
    fun injectMember(fakeInactivePhoneDependency: FakeInactivePhoneDependency)
}