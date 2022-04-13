package com.tokopedia.tokofood.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.home.presentation.fragment.TokoFoodHomeFragment
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodHomeViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodHomeComponent {
    fun inject(fragment: TokoFoodHomeFragment)
}