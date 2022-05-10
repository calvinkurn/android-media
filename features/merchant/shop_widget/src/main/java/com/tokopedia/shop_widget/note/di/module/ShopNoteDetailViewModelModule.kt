package com.tokopedia.shop_widget.note.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.shop_widget.note.di.scope.ShopNoteScope
import com.tokopedia.shop_widget.note.view.viewmodel.ShopNoteBottomSheetViewModel
import com.tokopedia.shop_widget.note.view.viewmodel.ShopNoteDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ShopNoteDetailViewModelModule {

    @ShopNoteScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ShopNoteDetailViewModel::class)
    internal abstract fun shopNoteDetailViewModel(viewModel: ShopNoteDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ShopNoteBottomSheetViewModel::class)
    internal abstract fun shopNoteBottomSheetViewModel(viewModel: ShopNoteBottomSheetViewModel): ViewModel
}