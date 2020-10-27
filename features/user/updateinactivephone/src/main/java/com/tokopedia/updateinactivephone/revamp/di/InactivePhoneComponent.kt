package com.tokopedia.updateinactivephone.revamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.updateinactivephone.revamp.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.revamp.di.module.InactivePhoneUseCaseModule
import com.tokopedia.updateinactivephone.revamp.di.module.InactivePhoneViewModelModule
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneDataUploadFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneOnboardingFragment
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
