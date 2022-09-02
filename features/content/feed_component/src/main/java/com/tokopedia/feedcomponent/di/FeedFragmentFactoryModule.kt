package com.tokopedia.feedcomponent.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.feedcomponent.view.factory.FeedFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 29, 2022
 */
@Module
abstract class FeedFragmentFactoryModule {

    @Binds
    abstract fun bindFragmentManager(fragmentFactory: FeedFragmentFactory): FragmentFactory
}