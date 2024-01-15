package com.tokopedia.home_explore_category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home_explore_category.presentation.viewmodel.ExploreCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExploreCategoryViewModelModule {

    @ExploreCategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ExploreCategoryScope
    @Binds
    @IntoMap
    @ViewModelKey(ExploreCategoryViewModel::class)
    internal abstract fun provideExploreCategoryViewModel(viewModel: ExploreCategoryViewModel): ViewModel
}
