package com.tokopedia.notifcenter.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 30/08/18.
 */

@Module
class NotifCenterModule {

    @Provides
    fun provideAnalyticTracker(@ApplicationContext context : Context) : AnalyticTracker{
        return (context as AbstractionRouter).analyticTracker
    }
}