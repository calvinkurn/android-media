package com.tokopedia.home_explore_category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.exploreCategory.viewmodel.ECServiceViewModel
import com.tokopedia.home_explore_category.di.ExploreCategoryScope
import com.tokopedia.home_explore_category.presentation.viewmodel.ExploreCategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ExploreCategoryViewModelModule {

    @ExploreCategoryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ExploreCategoryScope
    @ViewModelKey(ExploreCategoryViewModel::class)
    internal abstract fun provideExploreCategoryViewModel(viewModel: ExploreCategoryViewModel): ViewModel

}
