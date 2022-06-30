package com.tokopedia.wishlistcollection.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.viewmodel.BottomSheetAddCollectionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BottomSheetWishlistCollectionViewModelModule {
    @ActivityScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(BottomSheetAddCollectionViewModel::class)
    internal abstract fun bottomSheetWishlistCollectionViewModel(viewModel: BottomSheetAddCollectionViewModel): ViewModel
}