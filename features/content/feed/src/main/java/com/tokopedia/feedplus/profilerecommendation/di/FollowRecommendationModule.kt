package com.tokopedia.feedplus.profilerecommendation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.FollowAllRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.GetFollowRecommendationUseCase
import com.tokopedia.feedplus.profilerecommendation.domain.usecase.SetOnboardingStatusUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
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
    @Named(GetFollowRecommendationUseCase.QUERY_FOLLOW_RECOMMENDATION)
    fun provideFollowRecommendationQuery(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_follow_recommendation)

    @FollowRecommendationScope
    @Provides
    fun provideFollowKolPostUseCase(@ApplicationContext context: Context): FollowKolPostGqlUseCase {
        return FollowKolPostGqlUseCase(context, GraphqlUseCase())
    }

    @FollowRecommendationScope
    @Provides
    @Named(FollowAllRecommendationUseCase.MUTATION_FOLLOW_ALL_RECOMMENDATION)
    fun provideFollowAllRecommendationMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_follow_all_recommendation)

    @FollowRecommendationScope
    @Provides
    @Named(SetOnboardingStatusUseCase.MUTATION_SET_ONBOARDING_STATUS)
    fun provideSetOnboardingStatusMutation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_set_onboarding_status)
}