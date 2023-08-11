package com.tokopedia.catalog.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oldcatalog.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @CatalogScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogDetailPageViewModel::class)
    internal abstract fun catalogDetailPageViewModel(viewModel: CatalogDetailPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogDetailProductListingViewModel::class)
    internal abstract fun catalogDetailProductListingViewModel(viewModel: CatalogDetailProductListingViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogAllReviewsViewModel::class)
    internal abstract fun catalogAllReviewsViewModel(viewModel: CatalogAllReviewsViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogProductComparisonViewModel::class)
    internal abstract fun catalogProductComparisonViewModel(viewModel: CatalogProductComparisonViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogForYouViewModel::class)
    internal abstract fun catalogForYouViewModel(viewModel: CatalogForYouViewModel): ViewModel
}
