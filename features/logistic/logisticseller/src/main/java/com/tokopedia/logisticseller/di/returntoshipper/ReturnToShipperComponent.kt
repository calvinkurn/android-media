package com.tokopedia.logisticseller.di.returntoshipper

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.returntoshipper.fragment.ReturnToShipperFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [ReturnToShipperModule::class, ReturnToShipperViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ReturnToShipperComponent {
    fun inject(returnToShipperFragment: ReturnToShipperFragment)
}
