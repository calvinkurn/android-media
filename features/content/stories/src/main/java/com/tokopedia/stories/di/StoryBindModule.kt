package com.tokopedia.stories.di

import com.tokopedia.stories.data.mapper.StoryMapper
import com.tokopedia.stories.data.mapper.StoryMapperImpl
import com.tokopedia.stories.data.repository.StoryRepository
import com.tokopedia.stories.data.repository.StoryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class StoryBindModule {

    @Binds
    @StoryScope
    abstract fun bindStoryMapper(mapper: StoryMapperImpl): StoryMapper

    @Binds
    @StoryScope
    abstract fun bindStoryRepository(repository: StoryRepositoryImpl): StoryRepository

}
