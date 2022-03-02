package com.tokopedia.logisticorder.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticorder.view.reschedule_pickup.ReschedulePickupFragment
import dagger.Component

@ActivityScope
@Component(modules = [ReschedulePickupModule::class], dependencies = [BaseAppComponent::class])
interface ReschedulePickupComponent {
    fun inject(reschedulePickupFragment: ReschedulePickupFragment)
}