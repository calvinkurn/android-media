package com.tokopedia.paylater.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.paylater.di.module.PdpSimulationModule
import com.tokopedia.paylater.di.module.ViewModelModule
import com.tokopedia.paylater.di.scope.PayLaterScope
import com.tokopedia.paylater.presentation.fragment.*
import com.tokopedia.paylater.presentation.widget.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.paylater.presentation.widget.bottomsheet.PayLaterSignupBottomSheet
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