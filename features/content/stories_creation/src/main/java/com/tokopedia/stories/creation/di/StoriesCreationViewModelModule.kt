package com.tokopedia.stories.creation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.stories.creation.view.viewmodel.StoriesCreationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
abstract class StoriesCreationViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StoriesCreationViewModel::class)
    abstract fun bindStoriesCreationViewModel(viewModel: StoriesCreationViewModel): ViewModel
}
