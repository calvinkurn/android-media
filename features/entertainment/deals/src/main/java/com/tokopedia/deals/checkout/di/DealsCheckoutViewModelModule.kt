package com.tokopedia.deals.checkout.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsCheckoutViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DealsCheckoutViewModel::class)
    abstract fun provideDealsCheckoutViewModel(viewModel: DealsCheckoutViewModel): ViewModel

    @Binds
    @DealsCheckoutScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
