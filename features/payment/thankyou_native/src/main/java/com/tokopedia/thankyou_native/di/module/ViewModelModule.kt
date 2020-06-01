package com.tokopedia.thankyou_native.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.thankyou_native.di.scope.ThankYouPageScope
import com.tokopedia.thankyou_native.presentation.viewModel.CheckWhiteListViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.DetailInvoiceViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ThanksPageDataViewModel::class)
    internal abstract fun thanksPageDataViewModel(viewModel: ThanksPageDataViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CheckWhiteListViewModel::class)
    internal abstract fun provideCheckWhiteListViewModel(viewModel: CheckWhiteListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailInvoiceViewModel::class)
    internal abstract fun provideDetailInvoiceViewModel(viewModel: DetailInvoiceViewModel): ViewModel


}