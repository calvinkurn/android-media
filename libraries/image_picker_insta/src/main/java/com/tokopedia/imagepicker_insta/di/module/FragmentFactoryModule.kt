package com.tokopedia.imagepicker_insta.di.module

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FragmentFactoryModule {

    @Binds
    abstract fun bindFragmentManager(fragmentFactory: TkpdFragmentFactory): FragmentFactory
}
