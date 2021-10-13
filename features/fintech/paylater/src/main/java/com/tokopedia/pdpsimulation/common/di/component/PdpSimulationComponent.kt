package com.tokopedia.pdpsimulation.common.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pdpsimulation.common.di.module.PdpSimulationModule
import com.tokopedia.pdpsimulation.common.di.module.ViewModelModule
import com.tokopedia.pdpsimulation.common.di.scope.PdpSimulationScope
import com.tokopedia.pdpsimulation.common.presentation.activity.PdpSimulationActivity
import com.tokopedia.pdpsimulation.common.presentation.fragment.PdpSimulationFragment
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.CreditCardSimulationFragment
import com.tokopedia.pdpsimulation.creditcard.presentation.tnc.CreditCardTncFragment
import com.tokopedia.pdpsimulation.paylater.presentation.detail.PayLaterOffersFragment
import dagger.Component

@PdpSimulationScope
@Component(modules =
[PdpSimulationModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface PdpSimulationComponent {

    fun inject(pdpSimulationFragment: PdpSimulationFragment)
    fun inject(payLaterOffersFragment: PayLaterOffersFragment)
    fun inject(creditCardRegistrationBottomSheet: CreditCardRegistrationBottomSheet)
    fun inject(creditCardSimulationFragment: CreditCardSimulationFragment)
    fun inject(creditCardTncFragment: CreditCardTncFragment)
    fun inject(pdpSimulationActivity: PdpSimulationActivity) {

    }
}