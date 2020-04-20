package com.tokopedia.home.beranda.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactory
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

@Module
class HomeMapperModule {

    @HomeScope
    @Provides
    fun provideHomeDataMapper(@ApplicationContext context: Context, visitableFactory: HomeVisitableFactory, trackingQueue: TrackingQueue) = HomeDataMapper(context, visitableFactory, trackingQueue)

    @HomeScope
    @Provides
    fun provideFeedTabMapper(): FeedTabMapper = FeedTabMapper()

    @HomeScope
    @Provides
    fun provideHomRecommendationMapper() = HomeRecommendationMapper()

}