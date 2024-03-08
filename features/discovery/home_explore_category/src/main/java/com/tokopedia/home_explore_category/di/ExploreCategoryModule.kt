package com.tokopedia.home_explore_category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ExploreCategoryModule {

    @Provides
    @ExploreCategoryScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ExploreCategoryScope
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)
}
