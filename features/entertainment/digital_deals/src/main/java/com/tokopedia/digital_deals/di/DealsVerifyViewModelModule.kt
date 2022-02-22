package com.tokopedia.digital_deals.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital_deals.di.scope.DealsScope
import com.tokopedia.digital_deals.view.viewmodel.DealsCheckoutViewModel
import com.tokopedia.digital_deals.view.viewmodel.DealsVerifyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DealsVerifyViewModelModule {

    @Binds
    @DealsScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DealsVerifyViewModel::class)
    abstract fun dealsVerifyViewModel(viewModel: DealsVerifyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsCheckoutViewModel::class)
    abstract fun dealsCheckoutViewModel(viewModel: DealsCheckoutViewModel): ViewModel

}