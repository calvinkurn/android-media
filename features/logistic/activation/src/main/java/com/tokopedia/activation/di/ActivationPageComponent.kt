package com.tokopedia.activation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.activation.ui.ActivationPageActivity
import com.tokopedia.activation.ui.ActivationPageFragment
import dagger.Component

@ActivationPageScope
@Component(modules = [ActivationPageModule::class, ActivationPageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ActivationPageComponent  {
    fun inject(activationPageActivity: ActivationPageActivity)
    fun inject(activationPageFragment: ActivationPageFragment)
}