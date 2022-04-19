package com.tokopedia.kategori.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.kategori.viewmodel.CategoryLevelOneViewModel
import com.tokopedia.kategori.viewmodel.CategoryLevelTwoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @CategoryNavigationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CategoryNavigationScope
    @ViewModelKey(CategoryLevelOneViewModel::class)
    internal abstract fun productLevelOneInfoViewModel(viewModel: CategoryLevelOneViewModel): ViewModel

    @Binds
    @IntoMap
    @CategoryNavigationScope
    @ViewModelKey(CategoryLevelTwoViewModel::class)
    internal abstract fun productLevelTwoInfoViewModel(viewModel: CategoryLevelTwoViewModel): ViewModel
}
