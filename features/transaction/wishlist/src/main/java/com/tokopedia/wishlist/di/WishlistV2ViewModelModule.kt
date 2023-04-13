package com.tokopedia.wishlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.wishlist.view.viewmodel.WishlistV2ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 16/10/21.
 */

@Module
abstract class WishlistV2ViewModelModule {
    @WishlistV2Scope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @WishlistV2Scope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistV2ViewModel::class)
    internal abstract fun wishlistV2ViewModel(viewModel: WishlistV2ViewModel): ViewModel
}
