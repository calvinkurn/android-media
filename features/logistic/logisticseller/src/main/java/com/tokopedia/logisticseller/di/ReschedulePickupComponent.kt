package com.tokopedia.logisticseller.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticseller.ui.reschedulepickup.ReschedulePickupActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [ReschedulePickupModule::class, ReschedulePickupViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ReschedulePickupComponent {
    fun inject(reschedulePickupActivity: ReschedulePickupActivity)
}
