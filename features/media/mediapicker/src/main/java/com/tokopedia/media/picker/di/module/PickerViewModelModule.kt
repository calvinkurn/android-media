package com.tokopedia.media.picker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.ui.activity.album.AlbumViewModel
import com.tokopedia.media.picker.ui.activity.picker.PickerViewModel
import com.tokopedia.media.picker.ui.fragment.camera.CameraViewModel
import com.tokopedia.media.picker.ui.fragment.permission.PermissionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PickerViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PickerViewModel::class)
    internal abstract fun getPickerViewModel(viewModel: PickerViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(AlbumViewModel::class)
    internal abstract fun getAlbumViewModel(viewModel: AlbumViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CameraViewModel::class)
    internal abstract fun getCameraViewModel(viewModel: CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PermissionViewModel::class)
    internal abstract fun getPermissionViewModel(viewModel: PermissionViewModel): ViewModel

}