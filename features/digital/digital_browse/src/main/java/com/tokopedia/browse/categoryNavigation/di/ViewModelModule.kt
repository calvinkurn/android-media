package com.tokopedia.browse.categoryNavigation.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelOneViewModel
import com.tokopedia.browse.categoryNavigation.viewmodel.CategoryLevelTwoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@CategoryNavigationScope
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
