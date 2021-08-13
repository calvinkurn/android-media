package com.tokopedia.saldodetails.commom.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.saldodetails.feature_detail_pages.penjualan.DepositHistoryInvoiceDetailViewModel
import com.tokopedia.saldodetails.feature_saldo_transaction_history.viewmodel.TransactionHistoryViewModel
import com.tokopedia.saldodetails.feature_detail_pages.withdrawal.WithdrawalDetailViewModel
import com.tokopedia.saldodetails.feature_merchant_details.priority.MerchantSaldoPriorityViewModel
import com.tokopedia.saldodetails.feature_saldo_detail.SaldoDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @SaldoDetailsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(SaldoDetailViewModel::class)
    internal abstract fun SaldoDetailVM(viewModel: SaldoDetailViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(MerchantSaldoPriorityViewModel::class)
    internal abstract fun MerchantSPViewModel(viewModel: MerchantSaldoPriorityViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(TransactionHistoryViewModel::class)
    internal abstract fun transactionHistoryViewModel(viewModel: TransactionHistoryViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(WithdrawalDetailViewModel::class)
    internal abstract fun withdrawalDetailViewModel(viewModel: WithdrawalDetailViewModel): ViewModel

    @SaldoDetailsScope
    @Binds
    @IntoMap
    @ViewModelKey(DepositHistoryInvoiceDetailViewModel::class)
    internal abstract fun depositHistoryInvoiceDetailViewModel(viewModel: DepositHistoryInvoiceDetailViewModel): ViewModel

}