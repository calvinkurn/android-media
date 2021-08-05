package com.tokopedia.common.topupbills.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TopupBillsViewModelModule {

    @CommonTopupBillsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopupBillsViewModel::class)
    internal abstract fun topupBillsViewModel(viewModel: TopupBillsViewModel): ViewModel
}