package com.tokopedia.commissionbreakdown.di.module

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import dagger.Binds
import dagger.Module

@Module
abstract class CommissionBreakdownViewModelModule {

    @Binds
    @SellerHomeScope
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}