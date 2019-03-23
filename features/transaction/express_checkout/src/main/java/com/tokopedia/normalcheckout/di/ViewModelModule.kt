package com.tokopedia.normalcheckout.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.normalcheckout.presenter.NormalCheckoutViewModel
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