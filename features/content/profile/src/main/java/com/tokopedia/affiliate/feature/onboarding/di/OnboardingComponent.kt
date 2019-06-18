package com.tokopedia.affiliate.feature.onboarding.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.affiliate.feature.onboarding.view.fragment.UsernameInputFragment

import dagger.Component

/**
 * @author by milhamj on 10/4/18.
 */
@OnboardingScope
@Component(dependencies = [BaseAppComponent::class], modules = [OnboardingModule::class])
interface OnboardingComponent {
    fun inject(fragment: UsernameInputFragment)
}
