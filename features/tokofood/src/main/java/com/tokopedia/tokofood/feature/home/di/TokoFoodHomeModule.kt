package com.tokopedia.tokofood.feature.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodCategoryAnalytics
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TokoFoodHomeModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    fun homeAnalytic(): TokoFoodHomeAnalytics {
        return TokoFoodHomeAnalytics()
    }

    @Provides
    fun homeAnalyticOld(): TokoFoodHomeAnalyticsOld {
        return TokoFoodHomeAnalyticsOld()
    }

    @Provides
    fun trackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }

    @Provides
    fun categoryAnalytic(): TokoFoodCategoryAnalytics {
        return TokoFoodCategoryAnalytics()
    }

    @Provides
    fun categoryAnalyticOld(): TokoFoodCategoryAnalyticsOld {
        return TokoFoodCategoryAnalyticsOld()
    }

}
