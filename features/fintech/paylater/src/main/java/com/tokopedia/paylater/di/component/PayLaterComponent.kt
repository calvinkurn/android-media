package com.tokopedia.paylater.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.paylater.di.module.GqlQueryModule
import com.tokopedia.paylater.di.module.PayLaterModule
import com.tokopedia.paylater.di.module.ViewModelModule
import com.tokopedia.paylater.di.scope.PayLaterScope
import com.tokopedia.paylater.presentation.activity.PayLaterActivity
import com.tokopedia.paylater.presentation.fragment.PayLaterFragment
import com.tokopedia.paylater.presentation.fragment.PayLaterOffersFragment
import com.tokopedia.paylater.presentation.fragment.SimulationFragment
import dagger.Component

@PayLaterScope
@Component(modules =
[PayLaterModule::class,
    ViewModelModule::class,
    GqlQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface PayLaterComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(payLaterFragment: PayLaterFragment)
    fun inject(payLaterOffersFragment: PayLaterOffersFragment)
    fun inject(simulationFragment: SimulationFragment)
}