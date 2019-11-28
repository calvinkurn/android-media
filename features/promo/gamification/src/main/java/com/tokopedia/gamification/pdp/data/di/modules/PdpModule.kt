package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PdpModule {

    @GamificationPdpScope
    @Provides
    fun getGamingRecommendationProductUseCase(@Named(GqlQueryModule.RECOMMENDATION_QUERY) qeury: String,
                                              graphqlUseCase: GraphqlUseCase,
                                              userSession: UserSessionInterface): GamingRecommendationProductUseCase {
        return GamingRecommendationProductUseCase(qeury, graphqlUseCase, userSession)
    }

    @GamificationPdpScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }


}