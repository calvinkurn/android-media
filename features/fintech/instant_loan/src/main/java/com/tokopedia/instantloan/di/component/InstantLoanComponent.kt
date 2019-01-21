package com.tokopedia.instantloan.di.component

import android.content.Context

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.instantloan.di.module.InstantLoanModule
import com.tokopedia.instantloan.di.scope.InstantLoanScope
import com.tokopedia.instantloan.view.activity.InstantLoanActivity
import com.tokopedia.instantloan.view.fragment.DanaInstantFragment
import com.tokopedia.instantloan.view.fragment.DenganAgunanFragment
import com.tokopedia.instantloan.view.fragment.TanpaAgunanFragment

import dagger.Component

@InstantLoanScope
@Component(modules = [InstantLoanModule::class], dependencies = [BaseAppComponent::class])
interface InstantLoanComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(instantLoanActivity: InstantLoanActivity)

    fun inject(denganAgunanFragment: DenganAgunanFragment)

    fun inject(danaInstantFragment: DanaInstantFragment)

    fun inject(tanpaAgunanFragment: TanpaAgunanFragment)
}

