package com.tokopedia.categoryLevels.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.categoryLevels.viewmodel.CatalogNavViewModel
import com.tokopedia.categoryLevels.viewmodel.CategoryNavViewModel
import com.tokopedia.categoryLevels.viewmodel.ProductNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@com.tokopedia.categoryLevels.di.CategoryNavScope
abstract class CategoryViewModelModule {

    @Binds
    @com.tokopedia.categoryLevels.di.CategoryNavScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @com.tokopedia.categoryLevels.di.CategoryNavScope
    @ViewModelKey(ProductNavViewModel::class)
    internal abstract fun productNavViewModel(viewModel: ProductNavViewModel): ViewModel

    @Binds
    @IntoMap
    @com.tokopedia.categoryLevels.di.CategoryNavScope
    @ViewModelKey(CatalogNavViewModel::class)
    internal abstract fun catalogNavViewModel(viewModel: CatalogNavViewModel): ViewModel

    @Binds
    @IntoMap
    @com.tokopedia.categoryLevels.di.CategoryNavScope
    @ViewModelKey(CategoryNavViewModel::class)
    internal abstract fun categoryNavViewModel(viewModel: CategoryNavViewModel): ViewModel
}