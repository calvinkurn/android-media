package com.tokopedia.ovop2p.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.ovop2p.viewmodel.GetWalletBalanceViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2PTransactionThankYouVM
import com.tokopedia.ovop2p.viewmodel.OvoP2pTransferRequestViewModel
import com.tokopedia.ovop2p.viewmodel.OvoP2pTrxnConfirmVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OvoP2PViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GetWalletBalanceViewModel::class)
    internal abstract fun bindsWalletBalanceViewModel(viewModel: GetWalletBalanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OvoP2pTransferRequestViewModel::class)
    internal abstract fun bindsTransferRequestViewModel(viewModel: OvoP2pTransferRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OvoP2pTrxnConfirmVM::class)
    internal abstract fun bindsTrxnConfirmViewModel(viewModel: OvoP2pTrxnConfirmVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OvoP2PTransactionThankYouVM::class)
    internal abstract fun bindsTxnThankYouViewModel(viewModel: OvoP2PTransactionThankYouVM): ViewModel
}