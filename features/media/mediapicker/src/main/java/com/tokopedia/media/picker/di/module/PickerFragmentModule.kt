package com.tokopedia.media.picker.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PickerFragmentModule {

    @Binds
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PermissionFragment::class)
    internal abstract fun bindPermissionFragment(fragment: PermissionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CameraFragment::class)
    internal abstract fun bindCameraFragment(fragment: CameraFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GalleryFragment::class)
    internal abstract fun bindGalleryFragment(fragment: GalleryFragment): Fragment

}