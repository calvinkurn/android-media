package com.tokopedia.onboarding.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.onboarding.view.fragment.OnboardingFragment
import dagger.Component

/**
 * Created by Ade Fulki on 2020-02-09.
 * ade.hadian@tokopedia.com
 */

@OnboardingScope
@Component(
        modules = [OnboardingModule::class],
        dependencies = [BaseAppComponent::class]
)
interface OnboardingComponent {
    fun inject(fragment: OnboardingFragment)
}