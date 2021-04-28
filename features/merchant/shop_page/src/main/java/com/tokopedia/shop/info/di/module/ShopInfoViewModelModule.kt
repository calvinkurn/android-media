package com.tokopedia.shop.info.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop.info.di.scope.ShopInfoScope
import com.tokopedia.shop.info.view.viewmodel.ShopInfoViewModel
import com.tokopedia.shop.note.view.viewmodel.ShopNoteBottomSheetViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopInfoViewModelModule {

    @ShopInfoScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopInfoViewModel::class)
    internal abstract fun shopInfoViewModel(viewModel: ShopInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopNoteBottomSheetViewModel::class)
    internal abstract fun shopNoteBottomSheetViewModel(viewModel: ShopNoteBottomSheetViewModel): ViewModel
}