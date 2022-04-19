package com.tokopedia.salam.umrah.checkout.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.salam.umrah.checkout.presentation.viewmodel.UmrahCheckoutViewModel
import com.tokopedia.salam.umrah.orderdetail.di.UmrahOrderDetailScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UmrahCheckoutViewModelModule {

    @UmrahCheckoutScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(UmrahCheckoutViewModel::class)
    internal abstract fun umrahChekcoutViewModel(viewModel: UmrahCheckoutViewModel): ViewModel

}