package com.tokopedia.tokopedianow.repurchase.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.repurchase.di.scope.RepurchaseScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RepurchaseModule {

    @RepurchaseScope
    @Provides
    fun provideRepurchaseAnalytic(): RepurchaseAnalytics {
        return RepurchaseAnalytics()
    }

    @RepurchaseScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @RepurchaseScope
    @Provides
    fun provideGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
    
    @RepurchaseScope
    @Provides
    fun provideGetRecommendationUseCase(@ApplicationContext context: Context, coroutineGqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)
}