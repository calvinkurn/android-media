package com.tokopedia.stories.di

import com.tokopedia.stories.analytics.StoriesSharingAnalytics
import com.tokopedia.stories.analytics.StoriesSharingAnalyticsImpl
import com.tokopedia.stories.analytics.StoriesRoomAnalytic
import com.tokopedia.stories.analytics.StoriesRoomAnalyticImpl
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

    /**
     * Analytic
     */

    @Binds
    @StoriesScope
    abstract fun bindSharingAnalytic(analytic: StoriesSharingAnalyticsImpl.Factory): StoriesSharingAnalytics.Factory
    @Binds
    @StoriesScope
    abstract fun bindStoriesRoomAnalytic(analytic: StoriesRoomAnalyticImpl.Factory): StoriesRoomAnalytic.Factory

}
