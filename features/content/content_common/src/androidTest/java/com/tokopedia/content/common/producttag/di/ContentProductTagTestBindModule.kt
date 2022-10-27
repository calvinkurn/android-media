package com.tokopedia.content.common.producttag.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@Module
abstract class ContentProductTagTestBindModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory
}