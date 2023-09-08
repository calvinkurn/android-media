package com.tokopedia.stories.creation.di

import com.tokopedia.stories.creation.data.StoriesCreationRepositoryImpl
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
abstract class StoriesCreationBindModule {

    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationRepository(repository: StoriesCreationRepositoryImpl): StoriesCreationRepository
}
