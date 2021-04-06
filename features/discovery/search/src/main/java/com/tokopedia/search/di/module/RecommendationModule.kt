package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import dagger.Provides
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import javax.inject.Named

@Module
class RecommendationModule {
    @Provides
    @SearchScope
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@SearchContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @Provides
    @SearchScope
    fun provideGetRecommendationUseCase(
        @SearchContext context: Context,
        @Named("recommendationQuery") recomQuery: String,
        graphqlUseCase: GraphqlUseCase,
        userSessionInterface: UserSessionInterface
    ): GetRecommendationUseCase {
        return GetRecommendationUseCase(context, recomQuery, graphqlUseCase, userSessionInterface)
    }
}