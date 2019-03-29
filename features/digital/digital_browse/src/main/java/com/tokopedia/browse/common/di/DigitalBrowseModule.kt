package com.tokopedia.browse.common.di

import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.browse.common.DigitalBrowseRouter
import com.tokopedia.browse.common.data.DigitalBrowseRepositoryImpl
import com.tokopedia.browse.common.domain.DigitalBrowseRepository

import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 30/08/18.
 */

@Module
class DigitalBrowseModule {

    @DigitalBrowseScope
    @Provides
    fun provideAnalyticTracker(@ApplicationContext context: Context): AnalyticTracker {
        if (context is AbstractionRouter) {
            return (context as AbstractionRouter).analyticTracker
        }
        throw RuntimeException("App should implement " + AbstractionRouter::class.java.simpleName)
    }

    @DigitalBrowseScope
    @Provides
    fun provideDigitalBrowseRouter(@ApplicationContext context: Context): DigitalBrowseRouter {
        if (context is DigitalBrowseRouter) {
            return context
        }
        throw RuntimeException("Application must implement " + DigitalBrowseRouter::class.java.canonicalName)
    }

    @DigitalBrowseScope
    @Provides
    fun provideDigitalBrowseRepository(): DigitalBrowseRepository {
        return DigitalBrowseRepositoryImpl()
    }
}
