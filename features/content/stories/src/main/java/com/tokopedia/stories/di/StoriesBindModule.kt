package com.tokopedia.stories.di

import com.tokopedia.stories.data.mapper.StoriesMapper
import com.tokopedia.stories.data.mapper.StoriesMapperImpl
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.data.repository.StoriesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class StoriesBindModule {

    @Binds
    @StoriesScope
    abstract fun bindStoriesMapper(mapper: StoriesMapperImpl): StoriesMapper

    @Binds
    @StoriesScope
    abstract fun bindStoriesRepository(repository: StoriesRepositoryImpl): StoriesRepository

}
