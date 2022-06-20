package com.tokopedia.logisticorder.view.pod.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticorder.view.TrackingPageFragment
import com.tokopedia.logisticorder.view.bottomsheet.DriverTippingBottomSheet
import com.tokopedia.logisticorder.view.pod.ui.ProofOfDeliveryFragment
import dagger.Component
/**
 * Created by irpan on 28/04/22.
 */
@ActivityScope
@Component(modules = [ProofOfDeliveryModule::class], dependencies = [BaseAppComponent::class])
interface ProofOfDeliveryComponent {
    fun inject(fragment: ProofOfDeliveryFragment)
}