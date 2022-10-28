package com.tokopedia.attachvoucher.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.attachvoucher.view.viewmodel.AttachVoucherViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AttachVoucherViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AttachVoucherViewModel::class)
    abstract fun provideAttachVoucherViewModel(viewModel: AttachVoucherViewModel): ViewModel

    @Binds
    @AttachVoucherScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}