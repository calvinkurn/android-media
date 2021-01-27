package com.tokopedia.travel_slice.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travel_slice.analytics.TravelSliceAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module
class TravelSliceModule {
    @TravelSliceScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @TravelSliceScope
    @Provides
    fun providerUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @TravelSliceScope
    @Provides
    fun provideTravelSliceAnalytics(userSession: UserSessionInterface): TravelSliceAnalytics = TravelSliceAnalytics(userSession)
}