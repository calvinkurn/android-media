package com.tokopedia.digital.common.di


import android.content.Context

import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.common.router.DigitalModuleRouter

import dagger.Module
import dagger.Provides

@Module
class DigitalModule {

    @Provides
    fun provideDigitalAnalytics(router: AbstractionRouter, @ApplicationContext context: Context): DigitalAnalytics {
        return DigitalAnalytics(router.analyticTracker, context)
    }

    @Provides
    fun provideDigitalModuleRouter(@ApplicationContext context: Context): DigitalModuleRouter {
        val router : DigitalModuleRouter = context as DigitalModuleRouter
        return router
    }
}
