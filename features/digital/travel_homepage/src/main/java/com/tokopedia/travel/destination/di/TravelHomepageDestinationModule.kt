package com.tokopedia.travel.destination.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingUtil
import com.tokopedia.travel.homepage.usecase.GetEmptyViewModelsUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by jessicasean on 12/23/2019
 */
@TravelHomepageDestinationScope
@Module
class TravelHomepageDestinationModule {

    @TravelHomepageDestinationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @TravelHomepageDestinationScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @TravelHomepageDestinationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @TravelHomepageDestinationScope
    @Provides
    fun provideTravelHomepageTrackingUtil(): TravelHomepageTrackingUtil = TravelHomepageTrackingUtil()

}