package com.tokopedia.picker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.picker.di.scope.PickerScope
import com.tokopedia.picker.ui.fragment.gallery.GalleryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PickerViewModelModule {

    @Binds
    @PickerScope
    internal abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @PickerScope
    @ViewModelKey(GalleryViewModel::class)
    internal abstract fun getGalleryViewModel(
        viewModel: GalleryViewModel
    ): ViewModel

}