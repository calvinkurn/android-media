package com.tokopedia.discovery.categoryrevamp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.discovery.categoryrevamp.viewmodel.CatalogNavViewModel
import com.tokopedia.discovery.categoryrevamp.viewmodel.CategoryNavViewModel
import com.tokopedia.discovery.categoryrevamp.viewmodel.ProductNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@CategoryNavScope
abstract class CategoryViewModelModule {

    @Binds
    @CategoryNavScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CategoryNavScope
    @ViewModelKey(ProductNavViewModel::class)
    internal abstract fun productNavViewModel(viewModel: ProductNavViewModel): ViewModel

    @Binds
    @IntoMap
    @CategoryNavScope
    @ViewModelKey(CatalogNavViewModel::class)
    internal abstract fun catalogNavViewModel(viewModel: CatalogNavViewModel): ViewModel

    @Binds
    @IntoMap
    @CategoryNavScope
    @ViewModelKey(CategoryNavViewModel::class)
    internal abstract fun categoryNavViewModel(viewModel: CategoryNavViewModel): ViewModel
}