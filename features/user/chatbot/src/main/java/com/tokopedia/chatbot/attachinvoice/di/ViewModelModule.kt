package com.tokopedia.chatbot.attachinvoice.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.chatbot.attachinvoice.view.viewmodel.TransactionInvoiceListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TransactionInvoiceListViewModel::class)
    internal abstract fun provideTransactionInvoiceListViewModel(viewModel: TransactionInvoiceListViewModel): ViewModel

}