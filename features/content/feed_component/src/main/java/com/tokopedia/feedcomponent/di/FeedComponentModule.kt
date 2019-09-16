package com.tokopedia.feedcomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase.Companion.SEARCH_PROFILE_QUERY
import com.tokopedia.feedcomponent.domain.usecase.GetRelatedPostUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module
class FeedComponentModule {
    @Named(GetRelatedPostUseCase.RELATED_POST_KEY)
    @Provides
    fun provideQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            R.raw.query_related_post
        )
    }

    @Provides
    @Named(SEARCH_PROFILE_QUERY)
    fun provideGetMentionableUserQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_search_profile)
    }

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}