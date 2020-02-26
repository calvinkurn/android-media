package com.tokopedia.thankyou_native.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.thankyou_native.presentation.viewModel.OrderDetailListMapperViewModel
import com.tokopedia.thankyou_native.presentation.viewModel.ThanksPageDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ThankYouPageScope
@Module
abstract class ViewModelModule {

    @ThankYouPageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ThanksPageDataViewModel::class)
    internal abstract fun thanksPageDataViewModel(viewModel: ThanksPageDataViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailListMapperViewModel::class)
    internal abstract fun provideOrderListMapperViewModel(viewModel: OrderDetailListMapperViewModel): ViewModel

}