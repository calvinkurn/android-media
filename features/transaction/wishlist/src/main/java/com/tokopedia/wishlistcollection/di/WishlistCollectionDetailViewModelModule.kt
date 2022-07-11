package com.tokopedia.wishlistcollection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.wishlistcollection.view.viewmodel.WishlistCollectionDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WishlistCollectionDetailViewModelModule {
    @WishlistCollectionDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @WishlistCollectionDetailScope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistCollectionDetailViewModel::class)
    internal abstract fun wishlistCollectionDetailViewModel(viewModel: WishlistCollectionDetailViewModel): ViewModel
}