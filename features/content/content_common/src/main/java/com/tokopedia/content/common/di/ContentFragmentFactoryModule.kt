package com.tokopedia.content.common.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
@Module
abstract class ContentFragmentFactoryModule {

    @Binds
    abstract fun bindFragmentManager(fragmentFactory: TkpdFragmentFactory): FragmentFactory
}