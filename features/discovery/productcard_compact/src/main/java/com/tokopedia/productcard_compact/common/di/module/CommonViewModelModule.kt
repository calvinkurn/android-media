package com.tokopedia.productcard_compact.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.productcard_compact.common.di.scope.CommonScope
import com.tokopedia.productcard_compact.productcard.presentation.viewmodel.ProductCardCompactWishlistViewModel
import com.tokopedia.productcard_compact.similarproduct.presentation.viewmodel.TokoNowSimilarProductViewModel
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

    @Binds
    @CommonScope
    @IntoMap
    @ViewModelKey(TokoNowSimilarProductViewModel::class)
    internal abstract fun similarProductViewModel(viewModel: TokoNowSimilarProductViewModel): ViewModel
}
