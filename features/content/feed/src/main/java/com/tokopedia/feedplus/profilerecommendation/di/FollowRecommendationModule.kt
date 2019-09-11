package com.tokopedia.feedplus.profilerecommendation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.GetFollowRecommendationUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by jegul on 2019-09-11.
 */
@Module
class FollowRecommendationModule {

    @FollowRecommendationScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @FollowRecommendationScope
    @Provides
    @Named(GetFollowRecommendationUseCase.FOLLOW_RECOMMENDATION_QUERY)
    fun provideFollowRecommendationQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_follow_recommendation)
}