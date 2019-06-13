package com.tokopedia.hotel.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.common.di.scope.HotelScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 25/03/19
 */
@HotelScope
@Module
class HotelModule {

    @HotelScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @HotelScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @HotelScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}