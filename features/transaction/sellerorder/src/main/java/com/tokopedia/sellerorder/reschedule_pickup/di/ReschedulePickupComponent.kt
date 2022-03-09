package com.tokopedia.sellerorder.reschedule_pickup.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.sellerorder.reschedule_pickup.presentation.fragment.ReschedulePickupFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [ReschedulePickupModule::class, ReschedulePickupViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ReschedulePickupComponent {
    fun inject(reschedulePickupFragment: ReschedulePickupFragment)
}