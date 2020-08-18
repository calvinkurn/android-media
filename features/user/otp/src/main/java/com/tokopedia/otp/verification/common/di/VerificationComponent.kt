package com.tokopedia.otp.verification.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.fragment.*
import dagger.Component

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@VerificationScope
@Component(modules = [
    VerificationModule::class,
    VerificationViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface VerificationComponent{
    fun inject(activity: VerificationActivity)
    fun inject(fragment: VerificationFragment)
    fun inject(fragment: OnboardingMiscallFragment)
    fun inject(fragment: VerificationMethodFragment)
}