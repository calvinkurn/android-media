package com.tokopedia.stories.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.stories.view.fragment.StoryDetailFragment
import com.tokopedia.stories.view.fragment.StoryGroupFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StoryFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(StoryGroupFragment::class)
    abstract fun bindStoryFragment(fragment: StoryGroupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(StoryDetailFragment::class)
    abstract fun bindStoryContentFragment(fragment: StoryDetailFragment): Fragment

}
