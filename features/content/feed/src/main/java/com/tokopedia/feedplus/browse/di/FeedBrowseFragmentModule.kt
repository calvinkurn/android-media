package com.tokopedia.feedplus.browse.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.feedplus.browse.presentation.FeedBrowseFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by meyta.taliti on 14/08/23.
 */
@Module
abstract class FeedBrowseFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(FeedBrowseFragment::class)
    abstract fun getFeedBrowseFragment(fragment: FeedBrowseFragment): Fragment
}
