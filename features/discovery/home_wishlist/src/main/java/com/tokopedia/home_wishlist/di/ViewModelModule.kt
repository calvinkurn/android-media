package com.tokopedia.home_wishlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.home_wishlist.viewmodel.WishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * A class dagger module for handling viewModel
 */
@Module
abstract class ViewModelModule {
    @Binds
    @WishlistScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WishlistViewModel::class)
    internal abstract fun wishlistViewModel(viewModel: WishlistViewModel): ViewModel
}