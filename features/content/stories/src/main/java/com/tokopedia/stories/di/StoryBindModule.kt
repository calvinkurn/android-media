package com.tokopedia.stories.di

import com.tokopedia.stories.data.repository.StoryRepository
import com.tokopedia.stories.data.repository.StoryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class StoryBindModule {

    @Binds
    @StoryScope
    abstract fun bindStoryRepository(repository: StoryRepositoryImpl): StoryRepository

}
