package com.tokopedia.gamification.pdp.data.di.modules

import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PdpModule {

    @GamificationPdpScope
    @Provides
    fun getRecommendationUseCase(@Named(GqlQueryModule.RECOMMENDATION_QUERY) qeury: String,
                                 graphqlUseCase: GraphqlUseCase,
                                 userSession: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(qeury, graphqlUseCase, userSession)
    }

    @GamificationPdpScope
    @Provides
    fun getGamingRecommendationParamUseCase(@Named(GqlQueryModule.GAMING_RECOMMENDATION_PARAM_QUERY) qeury: String,
                                 graphqlUseCase: GraphqlUseCase,
                                 userSession: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(qeury, graphqlUseCase, userSession)
    }



}