package com.tokopedia.hotel.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.HotelModuleRouter
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.scope.HotelScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import javax.inject.Named

/**
 * @author by furqan on 25/03/19
 */
@HotelScope
@Module
class HotelModule {

    @HotelScope
    @Provides
    fun provideHotelModuleRouter(@ApplicationContext context: Context): HotelModuleRouter {
        if (context is HotelModuleRouter) {
            return context
        }
        throw RuntimeException("App should implement " + HotelModuleRouter::class.java.simpleName)
    }

    @HotelScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @HotelScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @HotelScope
    @Provides
    @Named("dummy_search_result")
    fun provideDummySearchResult(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.dummy_search_result)

}