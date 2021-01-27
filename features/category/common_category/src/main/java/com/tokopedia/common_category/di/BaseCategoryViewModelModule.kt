package com.tokopedia.common_category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.common_category.viewmodel.BannedProdNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class BaseCategoryViewModelModule {

    @Binds
    @BaseCategoryNavScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @BaseCategoryNavScope
    @ViewModelKey(BannedProdNavViewModel::class)
    internal abstract fun bannedProdNavViewModel(viewModel: BannedProdNavViewModel): ViewModel
}