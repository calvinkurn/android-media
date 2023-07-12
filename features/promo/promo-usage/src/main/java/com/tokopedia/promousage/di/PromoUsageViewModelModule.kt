package com.tokopedia.promousage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.promousage.view.PromoUsageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PromoUsageViewModelModule {

    @Binds
    @PromoUsageScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @PromoUsageScope
    @ViewModelKey(PromoUsageViewModel::class)
    internal abstract fun bindPromoUsageViewModel(viewModel: PromoUsageViewModel): ViewModel
}
