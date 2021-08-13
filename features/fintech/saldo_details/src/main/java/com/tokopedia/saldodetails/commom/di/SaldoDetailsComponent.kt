package com.tokopedia.saldodetails.commom.di

import android.content.Context
import com.tokopedia.saldodetails.feature_detail_pages.penjualan.SaldoSalesDetailActivity
import com.tokopedia.saldodetails.feature_detail_pages.penjualan.SaldoSalesDetailFragment
import com.tokopedia.saldodetails.feature_detail_pages.withdrawal.SaldoWithdrawalDetailActivity
import com.tokopedia.saldodetails.feature_detail_pages.withdrawal.SaldoWithdrawalDetailFragment
import com.tokopedia.saldodetails.feature_merchant_details.credit.MerchantCreditDetailFragment
import com.tokopedia.saldodetails.feature_merchant_details.priority.MerchantSaldoPriorityFragment
import com.tokopedia.saldodetails.feature_saldo_detail.SaldoDepositActivity
import com.tokopedia.saldodetails.feature_saldo_detail.SaldoDepositFragment
import com.tokopedia.saldodetails.feature_saldo_hold_info.SaldoHoldInfoActivity
import com.tokopedia.saldodetails.feature_saldo_transaction_history.ui.SaldoTransactionHistoryFragment
import com.tokopedia.saldodetails.feature_saldo_transaction_history.ui.SaldoTransactionListFragment
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
