package com.tokopedia.stories.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.stories.common.StoriesAvatarViewModel
import com.tokopedia.stories.common.data.StoriesAvatarRepositoryImpl
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@Module
internal interface StoriesAvatarModule {

    @Binds
    fun bindRepository(repository: StoriesAvatarRepositoryImpl): StoriesAvatarRepository

    @Binds
    @StoriesAvatarScope
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StoriesAvatarViewModel::class)
    fun bindStoriesAvatarViewModel(viewModel: StoriesAvatarViewModel): ViewModel
}
