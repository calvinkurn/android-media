package com.tokopedia.stories.widget.settings

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by astidhiyaa on 4/18/24
 */
@Module
abstract class StoriesSettingsModule {
    @Binds
    @IntoMap
    @FragmentKey(StoriesSettingsFragment::class)
    abstract fun bindFragment(fragment: StoriesSettingsFragment): Fragment

    @Binds
    abstract fun bindRepo(repo: StoriesSettingsRepo): StoriesSettingsRepository
}
