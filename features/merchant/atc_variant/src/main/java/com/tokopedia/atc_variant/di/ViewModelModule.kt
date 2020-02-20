package com.tokopedia.atc_variant.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.atc_variant.view.presenter.NormalCheckoutViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@NormalCheckoutScope
abstract class ViewModelModule {

    @NormalCheckoutScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NormalCheckoutViewModel::class)
    internal abstract fun normalCheckoutViewModel(viewModel: NormalCheckoutViewModel): ViewModel

}