package com.tokopedia.stories.creation.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.stories.creation.view.fragment.StoriesCreationFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
abstract class StoriesCreationFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(StoriesCreationFragment::class)
    abstract fun bindStoriesCreationFragment(fragment: StoriesCreationFragment): Fragment
}
