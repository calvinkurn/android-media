package com.tokopedia.common.topupbills.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common.topupbills.favorite.view.viewmodel.TopupBillsFavNumberViewModel
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(TopupBillsSavedNumberViewModel::class)
    internal abstract fun topupBillsSavedNumberViewModel(viewModel: TopupBillsSavedNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopupBillsFavNumberViewModel::class)
    internal abstract fun topupBillsFavNumberViewModel(viewModel: TopupBillsFavNumberViewModel): ViewModel
}