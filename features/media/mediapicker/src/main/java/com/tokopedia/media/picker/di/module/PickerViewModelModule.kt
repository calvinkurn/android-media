package com.tokopedia.media.picker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.media.picker.di.scope.PickerScope
import com.tokopedia.media.picker.ui.activity.album.AlbumViewModel
import com.tokopedia.media.picker.ui.activity.main.PickerViewModel
import com.tokopedia.media.picker.ui.fragment.camera.CameraViewModel
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryViewModel
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
    @ViewModelKey(PickerViewModel::class)
    internal abstract fun getPickerViewModel(
        viewModel: PickerViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @PickerScope
    @ViewModelKey(AlbumViewModel::class)
    internal abstract fun getAlbumViewModel(
        viewModel: AlbumViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @PickerScope
    @ViewModelKey(CameraViewModel::class)
    internal abstract fun getCameraViewModel(
        viewModel: CameraViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @PickerScope
    @ViewModelKey(GalleryViewModel::class)
    internal abstract fun getGalleryViewModel(
        viewModel: GalleryViewModel
    ): ViewModel

}