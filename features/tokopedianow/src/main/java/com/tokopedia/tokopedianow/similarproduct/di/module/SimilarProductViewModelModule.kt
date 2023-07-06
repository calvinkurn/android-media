package com.tokopedia.tokopedianow.similarproduct.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.similarproduct.di.scope.SimilarProductScope
import com.tokopedia.tokopedianow.similarproduct.presentation.viewmodel.TokoNowSimilarProductBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SimilarProductViewModelModule {

    @Binds
    @SimilarProductScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TokoNowSimilarProductBottomSheetViewModel::class)
    internal abstract fun tokoNowSimilarProductViewModel(viewModelTokoNowBottomSheet: TokoNowSimilarProductBottomSheetViewModel): ViewModel
}
