package com.tokopedia.saldodetails.di

import android.content.Context
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity
import com.tokopedia.saldodetails.view.activity.SaldoHoldInfoActivity
import com.tokopedia.saldodetails.view.fragment.MerchantCreditDetailFragment
import com.tokopedia.saldodetails.view.fragment.MerchantSaldoPriorityFragment
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment
import dagger.Component

@SaldoDetailsScope
@Component(modules = [
    ContextModule::class,
    SaldoDetailsModule::class,
    GqlQueryModule::class,
    DispatcherModule::class,
    ViewModelModule::class])
interface SaldoDetailsComponent {

    @SaldoDetailsScope
    fun context(): Context

    fun inject(fragment: MerchantSaldoPriorityFragment)

    fun inject(fragment: SaldoDepositFragment)

    fun inject(fragment: MerchantCreditDetailFragment)

    fun inject(saldoDepositActivity: SaldoDepositActivity)

    fun inject(saldoTransactionHistoryFragment: SaldoTransactionHistoryFragment)

    fun inject(saldoHoldInfoActivity: SaldoHoldInfoActivity)
}
