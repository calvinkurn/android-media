package com.tokopedia.travelhomepage.destination.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingUtil
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessicasean on 12/23/2019
 */
@TravelDestinationScope
@Module
class TravelDestinationModule {

    @TravelDestinationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @TravelDestinationScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @TravelDestinationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @TravelDestinationScope
    @Provides
    fun provideTravelHomepageTrackingUtil(): TravelHomepageTrackingUtil = TravelHomepageTrackingUtil()

}