package com.tokopedia.media.picker.common.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.media.picker.common.ui.fragment.TestCameraFragment
import com.tokopedia.media.picker.common.ui.fragment.TestGalleryFragment
import com.tokopedia.media.picker.common.ui.fragment.TestPermissionFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TestFragmentModule {

    @Binds
    internal abstract fun bindFragmentFactory(factory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(TestPermissionFragment::class)
    internal abstract fun bindTestPermissionFragment(fragment: TestPermissionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(TestCameraFragment::class)
    internal abstract fun bindTestCameraFragment(fragment: TestCameraFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(TestGalleryFragment::class)
    internal abstract fun bindTestGalleryFragment(fragment: TestGalleryFragment): Fragment

}