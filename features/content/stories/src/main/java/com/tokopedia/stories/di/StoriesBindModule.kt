package com.tokopedia.stories.di

import com.tokopedia.stories.data.StoriesRepository
import com.tokopedia.stories.data.StoriesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class StoriesBindModule {

    @Binds
    @StoriesScope
    abstract fun bindStoriesRepository(repository: StoriesRepositoryImpl): StoriesRepository

}
