package com.tokopedia.productcard.compact.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.productcard.compact.common.di.scope.CommonScope
import com.tokopedia.productcard.compact.productcard.presentation.viewmodel.ProductCardCompactWishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class CommonViewModelModule {
    @Binds
    @CommonScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @CommonScope
    @IntoMap
    @ViewModelKey(ProductCardCompactWishlistViewModel::class)
    internal abstract fun wishlistViewModel(viewModel: ProductCardCompactWishlistViewModel): ViewModel
}
