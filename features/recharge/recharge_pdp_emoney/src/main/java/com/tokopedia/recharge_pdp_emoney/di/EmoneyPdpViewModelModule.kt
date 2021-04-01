package com.tokopedia.recharge_pdp_emoney.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.recharge_pdp_emoney.presentation.viewmodel.EmoneyPdpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 29/03/21
 */

@Module
abstract class EmoneyPdpViewModelModule {

    @EmoneyPdpScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EmoneyPdpViewModel::class)
    internal abstract fun emoneyPdpViewModel(viewModel: EmoneyPdpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopupBillsViewModel::class)
    internal abstract fun topUpBillsViewModel(viewModel: TopupBillsViewModel): ViewModel
}