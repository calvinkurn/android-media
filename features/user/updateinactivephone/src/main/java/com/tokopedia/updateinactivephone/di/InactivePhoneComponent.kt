package com.tokopedia.updateinactivephone.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.di.module.InactivePhoneViewModelModule
import com.tokopedia.updateinactivephone.features.InactivePhoneActivity
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.features.onboarding.regular.InactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseInactivePhoneSubmitDataFragment
import com.tokopedia.updateinactivephone.features.submitnewphone.regular.InactivePhoneDataUploadFragment
import dagger.Component

@ActivityScope
@Component(modules = [
    InactivePhoneModule::class,
    InactivePhoneViewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface InactivePhoneComponent {
    fun inject(activity: InactivePhoneActivity)
    fun inject(activity: InactivePhoneAccountListActivity)

    fun inject(fragment: BaseInactivePhoneOnboardingFragment)
    fun inject(fragment: BaseInactivePhoneSubmitDataFragment)
    fun inject(fragment: InactivePhoneOnboardingFragment)
    fun inject(fragment: InactivePhoneDataUploadFragment)
}
