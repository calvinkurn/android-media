package com.tokopedia.onboarding.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.onboarding.OnboardingActivity
import com.tokopedia.onboarding.fragment.OnboardingFragment
import dagger.Component

/**
 * @author by stevenfredian on 12/12/18.
 */
@OnboardingScope
@Component(modules = arrayOf(OnboardingModule::class), dependencies = [(BaseAppComponent::class)])
interface OnboardingComponent {

    fun inject(activity: OnboardingActivity)

    fun inject(fragment: OnboardingFragment)

}