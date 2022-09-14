package com.tokopedia.feedcomponent.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.feedcomponent.domain.usecase.GetMentionableUserUseCase.Companion.SEARCH_PROFILE_QUERY
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides
import javax.inject.Named


@Module(includes = [TopAdsUrlHitterModule::class])
class FeedComponentModule {
    @Provides
    @Named(SEARCH_PROFILE_QUERY)
    fun provideGetMentionableUserQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_search_profile)
    }

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @Provides
    @Named(SUSPEND_GRAPHQL_REPOSITORY)
    fun provideSuspendGraphqlInteractor(): GraphqlRepository = Interactor.getInstance().graphqlRepository
}
