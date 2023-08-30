package com.tokopedia.logisticseller.ui.confirmshipping.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.confirmshipping.ui.ConfirmShippingActivity
import com.tokopedia.logisticseller.ui.confirmshipping.ui.ConfirmShippingFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-11-15.
 */
@ActivityScope
@Component(
    modules = [ConfirmShippingViewModelModule::class, ConfirmShippingModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ConfirmShippingComponent {
    fun inject(somConfirmShippingActivity: ConfirmShippingActivity)
    fun inject(somConfirmShippingFragment: ConfirmShippingFragment)
}
