package com.tokopedia.wishlistcollection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WishlistCollectionViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistCollectionViewModel::class)
    internal abstract fun collectionWishlistViewModel(viewModel: WishlistCollectionViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistCollectionDetailViewModel::class)
    internal abstract fun wishlistCollectionDetailViewModel(viewModel: WishlistCollectionDetailViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetAddCollectionViewModel::class)
    internal abstract fun bottomSheetWishlistCollectionViewModel(viewModel: BottomSheetAddCollectionViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetUpdateWishlistCollectionNameViewModel::class)
    internal abstract fun bottomSheetUpdateWishlistCollectionNameViewModel(viewModel: BottomSheetUpdateWishlistCollectionNameViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetCreateNewCollectionViewModel::class)
    internal abstract fun bottomSheetWishlistCreateNewCollectionViewModel(viewModel: BottomSheetCreateNewCollectionViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(WishlistCollectionEditViewModel::class)
    internal abstract fun wishlistCollectionEditViewModel(viewModel: WishlistCollectionEditViewModel): ViewModel
}
