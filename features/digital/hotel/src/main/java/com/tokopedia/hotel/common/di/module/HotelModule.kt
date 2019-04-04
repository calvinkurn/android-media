package com.tokopedia.hotel.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.hotel.common.di.scope.HotelScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers

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
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}