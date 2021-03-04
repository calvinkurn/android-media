package com.tokopedia.vouchergame.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common_digital.atc.DigitalAddToCartViewModel
import com.tokopedia.vouchergame.detail.view.viewmodel.VoucherGameDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 13/08/19
 */
@Module
abstract class VoucherGameDetailViewModelModule {

    @VoucherGameDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(VoucherGameDetailViewModel::class)
    internal abstract fun voucherGameDetailViewModel(viewModel: VoucherGameDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DigitalAddToCartViewModel::class)
    abstract fun bindDigitalAtcViewModel(viewModel: DigitalAddToCartViewModel): ViewModel
}