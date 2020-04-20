package com.tokopedia.smartbills.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@SmartBillsScope
abstract class SmartBillsViewModelModule {

    @SmartBillsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

//    @Binds
//    @IntoMap
//    @ViewModelKey(SmartBillsViewModel::class)
//    internal abstract fun rechargeCCViewModel(customViewModel: RechargeCCViewModel): ViewModel
}