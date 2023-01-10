package com.tokopedia.catalog_library.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.catalog_library.viewmodels.CatalogHomepageViewModel
import com.tokopedia.catalog_library.viewmodels.CatalogLandingPageViewModel
import com.tokopedia.catalog_library.viewmodels.CatalogLihatSemuaPageViewModel
import com.tokopedia.catalog_library.viewmodels.ProductsBaseViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @CatalogLibraryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogHomepageViewModel::class)
    internal abstract fun catalogHomepageViewModel(viewModel: CatalogHomepageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLihatSemuaPageViewModel::class)
    internal abstract fun catalogLihatSemuaPageViewModel(viewModel: CatalogLihatSemuaPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLandingPageViewModel::class)
    internal abstract fun catalogLandingPageViewModel(viewModel: CatalogLandingPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(ProductsBaseViewModel::class)
    internal abstract fun productsBaseViewModel(viewModel: ProductsBaseViewModel): ViewModel
}
