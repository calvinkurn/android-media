package com.tokopedia.wishlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.wishlist.view.viewmodel.WishlistV2CollectionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WishlistV2CollectionViewModelModule {
    @WishlistV2Scope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @WishlistV2Scope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistV2CollectionViewModel::class)
    internal abstract fun wishlistV2CollectionViewModel(viewModel: WishlistV2CollectionViewModel): ViewModel
}