package com.tokopedia.stories.di

import com.tokopedia.stories.analytic.StoriesAnalytic
import com.tokopedia.stories.analytic.StoriesAnalyticImpl
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

    @Binds
    @StoriesScope
    abstract fun bindStoriesAnalytic(analytic: StoriesAnalyticImpl): StoriesAnalytic

}
