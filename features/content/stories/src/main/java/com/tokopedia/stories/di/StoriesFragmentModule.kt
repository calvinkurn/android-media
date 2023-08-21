package com.tokopedia.stories.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.stories.bottomsheet.StoriesProductBottomSheet
import com.tokopedia.stories.bottomsheet.StoriesThreeDotsBottomSheet
import com.tokopedia.stories.view.fragment.StoriesDetailFragment
import com.tokopedia.stories.view.fragment.StoriesGroupFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StoriesFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(StoriesGroupFragment::class)
    abstract fun bindStoriesFragment(fragment: StoriesGroupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(StoriesDetailFragment::class)
    abstract fun bindStoriesContentFragment(fragment: StoriesDetailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(StoriesThreeDotsBottomSheet::class)
    abstract fun bindStoriesThreeDots(fragment: StoriesThreeDotsBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(StoriesProductBottomSheet::class)
    abstract fun bindStoriesProduct(fragment: StoriesProductBottomSheet): Fragment
}
