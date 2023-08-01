package com.tokopedia.stories.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.fragment.TkpdFragmentFactory
import com.tokopedia.stories.view.fragment.StoriesBaseFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StoriesFragmentModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: TkpdFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(StoriesBaseFragment::class)
    abstract fun bindStoriesBaseFragment(fragment: StoriesBaseFragment): Fragment

}
