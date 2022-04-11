package com.tokopedia.tokofood.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.home.presentation.fragment.TokoFoodHomeFragment
import dagger.Component

@Component(modules = [TokoFoodHomeViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodHomePromoComponent {
    fun inject(fragment: TokoFoodHomeFragment)
}