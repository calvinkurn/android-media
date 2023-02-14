package com.tokopedia.product.estimasiongkir.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class RatesEstimationModule {

    @RatesEstimationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @RatesEstimationScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}
