package com.tokopedia.saldodetails.commom.di.component

import android.content.Context
import com.tokopedia.saldodetails.commom.di.module.*
import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
import com.tokopedia.saldodetails.merchantDetail.priority.MerchantSaldoPriorityFragment
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositActivity
import com.tokopedia.saldodetails.saldoDetail.SaldoDepositFragment
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui.BaseSaldoTransactionListFragment
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui.SaldoTransactionHistoryFragment
import com.tokopedia.saldodetails.saldoHoldInfo.SaldoHoldInfoActivity
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.SaldoSalesDetailActivity
import com.tokopedia.saldodetails.transactionDetailPages.penjualan.SaldoSalesDetailFragment
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.SaldoWithdrawalDetailActivity
import com.tokopedia.saldodetails.transactionDetailPages.withdrawal.SaldoWithdrawalDetailFragment
import dagger.Component

@SaldoDetailsScope
@Component(
    modules = [
        ContextModule::class,
        SaldoDetailsModule::class,
        GqlQueryModule::class,
        DispatcherModule::class,
        ViewModelModule::class]
)
interface SaldoDetailsComponent {

    @SaldoDetailsScope
    fun context(): Context

    fun inject(fragment: MerchantSaldoPriorityFragment)

    fun inject(fragment: SaldoDepositFragment)

    fun inject(saldoDepositActivity: SaldoDepositActivity)

    fun inject(saldoTransactionHistoryFragment: SaldoTransactionHistoryFragment)

    fun inject(saldoHoldInfoActivity: SaldoHoldInfoActivity)
    fun inject(saldoWithdrawalDetailActivity: SaldoWithdrawalDetailActivity)
    fun inject(saldoSalesDetailActivity: SaldoSalesDetailActivity)
    fun inject(saldoWithdrawalDetailFragment: SaldoWithdrawalDetailFragment)
    fun inject(saldoSalesDetailFragment: SaldoSalesDetailFragment)
    fun inject(baseSaldoTransactionListFragment: BaseSaldoTransactionListFragment)

}
