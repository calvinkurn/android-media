package com.tokopedia.catalog_library.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.catalog_library.viewmodels.*
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
    @ViewModelKey(CatalogHomepageVM::class)
    internal abstract fun catalogHomepageViewModel(viewModel: CatalogHomepageVM): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLihatSemuaPageVM::class)
    internal abstract fun catalogLihatSemuaPageViewModel(viewModel: CatalogLihatSemuaPageVM): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLandingPageVM::class)
    internal abstract fun catalogLandingPageViewModel(viewModel: CatalogLandingPageVM): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogProductsBaseVM::class)
    internal abstract fun productsBaseViewModel(viewModel: CatalogProductsBaseVM): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogPopularBrandsVM::class)
    internal abstract fun popularBrandsViewModel(viewModel: CatalogPopularBrandsVM): ViewModel

}
