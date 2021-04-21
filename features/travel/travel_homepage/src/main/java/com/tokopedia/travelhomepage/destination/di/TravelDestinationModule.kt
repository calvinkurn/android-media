package com.tokopedia.travelhomepage.destination.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.destination.analytics.TravelDestinationTrackingUtil
import com.tokopedia.travelhomepage.destination.usecase.GetEmptyModelsUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by jessicasean on 12/23/2019
 */
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
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

    @TravelDestinationScope
    @Provides
    fun provideGetEmptyVMsUseCase(): GetEmptyModelsUseCase = GetEmptyModelsUseCase()

    @TravelDestinationScope
    @Provides
    fun provideTravelDestinationTrackingUtil(): TravelDestinationTrackingUtil = TravelDestinationTrackingUtil()

}