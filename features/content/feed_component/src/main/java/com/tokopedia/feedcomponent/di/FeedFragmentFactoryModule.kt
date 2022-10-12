package com.tokopedia.feedcomponent.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
@Module
abstract class FeedFragmentFactoryModule {

    @Binds
    abstract fun bindFragmentManager(fragmentFactory: TkpdFragmentFactory): FragmentFactory
}