package com.tokopedia.feedplus.di

import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import dagger.Binds
import dagger.Module

/**
 * Created by shruti on 16/03/23
 */
@Module
abstract class FeedFragmentModule {
    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory
}
