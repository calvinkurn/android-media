package com.tokopedia.digital.common.di


import com.tokopedia.digital.common.analytic.DigitalAnalytics
import dagger.Module
import dagger.Provides

@Module
class DigitalModule {

    @Provides
    fun provideDigitalAnalytics(): DigitalAnalytics {
        return DigitalAnalytics()
    }
}
