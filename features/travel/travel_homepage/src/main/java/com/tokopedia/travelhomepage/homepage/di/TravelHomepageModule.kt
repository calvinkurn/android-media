package com.tokopedia.travelhomepage.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelhomepage.homepage.analytics.TravelHomepageTrackingUtil
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 05/08/2019
 */
@Module
class TravelHomepageModule {

    @TravelHomepageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @TravelHomepageScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @TravelHomepageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @TravelHomepageScope
    @Provides
    fun provideTravelHomepageTrackingUtil(): TravelHomepageTrackingUtil = TravelHomepageTrackingUtil()

}