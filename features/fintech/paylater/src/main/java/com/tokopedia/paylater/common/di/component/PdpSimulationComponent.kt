package com.tokopedia.paylater.common.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.paylater.common.di.module.PdpSimulationModule
import com.tokopedia.paylater.common.di.module.ViewModelModule
import com.tokopedia.paylater.common.di.scope.PayLaterScope
import com.tokopedia.paylater.common.presentation.fragment.PdpSimulationFragment
import com.tokopedia.paylater.creditcard.presentation.registration.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.paylater.creditcard.presentation.simulation.CreditCardSimulationFragment
import com.tokopedia.paylater.creditcard.presentation.tnc.CreditCardTncFragment
import com.tokopedia.paylater.paylater.presentation.detail.PayLaterOffersFragment
import com.tokopedia.paylater.paylater.presentation.registration.PayLaterSignupBottomSheet
import com.tokopedia.paylater.paylater.presentation.simulation.PayLaterSimulationFragment

import dagger.Component

@PayLaterScope
@Component(modules =
[PdpSimulationModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface PdpSimulationComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(pdpSimulationFragment: PdpSimulationFragment)
    fun inject(payLaterOffersFragment: PayLaterOffersFragment)
    fun inject(simulationFragment: PayLaterSimulationFragment)
    fun inject(payLaterSignupBottomSheet: PayLaterSignupBottomSheet)
    fun inject(creditCardRegistrationBottomSheet: CreditCardRegistrationBottomSheet)
    fun inject(creditCardSimulationFragment: CreditCardSimulationFragment)
    fun inject(creditCardTncFragment: CreditCardTncFragment)
}