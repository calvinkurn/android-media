package com.tokopedia.smartbills.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.smartbills.presentation.viewmodel.SmartBillsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SmartBillsViewModelModule {

    @SmartBillsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SmartBillsViewModel::class)
    internal abstract fun rechargeCCViewModel(customViewModel: SmartBillsViewModel): ViewModel
}