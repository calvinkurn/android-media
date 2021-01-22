package com.tokopedia.thankyou_native.recommendation.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.thankyou_native.recommendation.di.module.GqlQueryModule.Companion.GQL_RECOMMENDATION_DATA
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RecommendationModule {

    @Provides
    fun provideGetRecommendationUseCase(@Named(GQL_RECOMMENDATION_DATA) query: String,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(query, graphqlUseCase, userSessionInterface)
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}