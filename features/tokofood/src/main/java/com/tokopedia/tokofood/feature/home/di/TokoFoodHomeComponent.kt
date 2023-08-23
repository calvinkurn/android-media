package com.tokopedia.tokofood.feature.home.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodCategoryFragment
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodHomeViewModelModule::class, TokoFoodHomeModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodHomeComponent {
    fun inject(fragment: TokoFoodHomeFragment)
    fun inject(fragment: TokoFoodCategoryFragment)
}
