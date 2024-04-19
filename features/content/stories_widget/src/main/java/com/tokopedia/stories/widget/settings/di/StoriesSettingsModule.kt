package com.tokopedia.stories.widget.settings.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsFragment
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepo
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepository
import com.tokopedia.stories.widget.settings.tracking.StoriesSettingsTracking
import com.tokopedia.stories.widget.settings.tracking.StoriesSettingsTrackingImpl
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

    @Binds
    abstract fun bindAnalytic(trackingImpl: StoriesSettingsTrackingImpl): StoriesSettingsTracking
}
