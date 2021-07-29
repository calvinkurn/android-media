package com.tokopedia.saldodetails.di

import android.content.Context
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity
import com.tokopedia.saldodetails.view.activity.SaldoHoldInfoActivity
import com.tokopedia.saldodetails.view.activity.SaldoSalesDetailActivity
import com.tokopedia.saldodetails.view.activity.SaldoWithdrawalDetailActivity
import com.tokopedia.saldodetails.view.fragment.*
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

    fun inject(fragment: SaldoTransactionListFragment)

    fun inject(fragment: MerchantSaldoPriorityFragment)

    fun inject(fragment: SaldoDepositFragment)

    fun inject(fragment: MerchantCreditDetailFragment)

    fun inject(saldoDepositActivity: SaldoDepositActivity)

    fun inject(saldoTransactionHistoryFragment: SaldoTransactionHistoryFragment)

    fun inject(saldoHoldInfoActivity: SaldoHoldInfoActivity)
    fun inject(saldoWithdrawalDetailActivity: SaldoWithdrawalDetailActivity)
    fun inject(saldoSalesDetailActivity: SaldoSalesDetailActivity)
    fun inject(saldoWithdrawalDetailFragment: SaldoWithdrawalDetailFragment)
    fun inject(saldoSalesDetailFragment: SaldoSalesDetailFragment)

}
