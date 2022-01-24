package com.tokopedia.pdpsimulation.activteGopay.di

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
@Component(dependencies = [BaseAppComponent::class])
interface ActivateGopayComponent {



}