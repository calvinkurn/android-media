package com.tokopedia.stories.widget.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsFragment
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepo
import com.tokopedia.stories.widget.settings.data.repository.StoriesSettingsRepository
import com.tokopedia.stories.widget.settings.presentation.viewmodel.StoriesSettingsViewModel
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
    @ActivityScope
    abstract fun bindRepo(repo: StoriesSettingsRepo): StoriesSettingsRepository

    @Binds
    @ActivityScope
    abstract fun bindAnalytic(trackingImpl: StoriesSettingsTrackingImpl): StoriesSettingsTracking

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StoriesSettingsViewModel::class)
    abstract fun provideViewModel(viewModel: StoriesSettingsViewModel): ViewModel
}
