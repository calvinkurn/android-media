package com.tokopedia.wishlistcollection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetCreateNewCollectionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BottomSheetCreateWishlistCollectionViewModelModule {
    @BottomSheetCreateWishlistCollectionScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @BottomSheetCreateWishlistCollectionScope
    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetCreateNewCollectionViewModel::class)
    internal abstract fun bottomSheetWishlistCollectionViewModel(viewModel: BottomSheetCreateNewCollectionViewModel): ViewModel
}