package com.tokopedia.phoneverification.di.revamp

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.phoneverification.view.activity.PhoneVerificationProfileActivity
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment
import dagger.Component


@PhoneVerificationScope
@Component(modules = [
    PhoneVerificationModule::class,
    PhoneVerificationUseCaseModule::class,
    PhoneVerificationViewModelModule::class,
    PhoneVerificationQueryModule::class]
, dependencies = [BaseAppComponent::class])
interface PhoneVerificationComponent {
    fun inject(activity: PhoneVerificationProfileActivity)
    fun inject(fragment: PhoneVerificationFragment)
}