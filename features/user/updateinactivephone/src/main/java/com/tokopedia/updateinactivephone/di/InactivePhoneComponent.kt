package com.tokopedia.updateinactivephone.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.di.module.InactivePhoneUseCaseModule
import com.tokopedia.updateinactivephone.di.module.InactivePhoneViewModelModule
import com.tokopedia.updateinactivephone.view.activity.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneDataUploadFragment
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneOnboardingFragment
import dagger.Component

@InactivePhoneScope
@Component(modules = [
    InactivePhoneModule::class,
    InactivePhoneUseCaseModule::class,
    InactivePhoneViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface InactivePhoneComponent {
    fun inject(activity: InactivePhoneAccountListActivity)

    fun inject(fragment: InactivePhoneOnboardingFragment)
    fun inject(fragment: InactivePhoneDataUploadFragment)
}
