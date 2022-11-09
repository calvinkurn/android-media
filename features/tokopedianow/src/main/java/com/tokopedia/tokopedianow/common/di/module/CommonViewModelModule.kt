package com.tokopedia.tokopedianow.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tokopedianow.common.di.scope.CommonScope
import com.tokopedia.tokopedianow.common.viewmodel.TokoNowWishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CommonViewModelModule {
    @Binds
    @CommonScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @CommonScope
    @IntoMap
    @ViewModelKey(TokoNowWishlistViewModel::class)
    internal abstract fun tokoNowWishlistViewModel(viewModel: TokoNowWishlistViewModel): ViewModel
}
