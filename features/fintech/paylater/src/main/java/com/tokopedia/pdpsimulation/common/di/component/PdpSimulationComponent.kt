package com.tokopedia.pdpsimulation.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment.ActivationCheckoutFragment
import com.tokopedia.pdpsimulation.common.di.module.PdpSimulationModule
import com.tokopedia.pdpsimulation.common.di.module.ViewModelModule
import com.tokopedia.pdpsimulation.common.di.scope.PdpSimulationScope
import com.tokopedia.pdpsimulation.common.presentation.activity.PdpSimulationActivity
import com.tokopedia.pdpsimulation.common.presentation.fragment.PdpSimulationFragment
import dagger.Component

@PdpSimulationScope
@Component(
    modules =
    [PdpSimulationModule::class,
        ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PdpSimulationComponent {

    fun inject(pdpSimulationFragment: PdpSimulationFragment)
    fun inject(activationCheckoutFragment: ActivationCheckoutFragment)
    fun inject(pdpSimulationActivity: PdpSimulationActivity) {

    }
}