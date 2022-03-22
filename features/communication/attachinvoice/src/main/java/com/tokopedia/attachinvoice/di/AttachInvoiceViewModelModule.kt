package com.tokopedia.attachinvoice.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.attachinvoice.view.viewmodel.AttachInvoiceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AttachInvoiceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AttachInvoiceViewModel::class)
    abstract fun provideChatSearchViewModel(viewModel: AttachInvoiceViewModel): ViewModel

    @Binds
    @AttachInvoiceScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}