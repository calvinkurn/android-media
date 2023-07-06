package com.tokopedia.tokofood.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivityOld
import dagger.Component

@TokoFoodScope
@Component(modules = [TokoFoodViewModelModule::class, TokoFoodModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodComponent {
    fun inject(activity: BaseTokofoodActivity)
    fun inject(activity: BaseTokofoodActivityOld)
}
