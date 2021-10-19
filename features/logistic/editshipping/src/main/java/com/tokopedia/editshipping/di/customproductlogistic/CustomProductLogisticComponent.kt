package com.tokopedia.editshipping.di.customproductlogistic

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editshipping.ui.customproductlogistic.CustomProductLogisticFragment
import dagger.Component

@ActivityScope
@Component(modules = [CustomProductLogisticViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CustomProductLogisticComponent {
    fun inject(customProductLogisticFragment: CustomProductLogisticFragment)
}