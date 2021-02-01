package com.tokopedia.paylater.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.paylater.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.paylater.paylater.viewModel.PayLaterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PayLaterViewModel::class)
    internal abstract fun bindsPayLaterViewModel(viewModel: PayLaterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreditCardViewModel::class)
    internal abstract fun bindsCreditCardViewModel(viewModel: CreditCardViewModel): ViewModel


}