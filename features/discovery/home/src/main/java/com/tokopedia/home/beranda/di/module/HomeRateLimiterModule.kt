package com.tokopedia.home.beranda.di.module

import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.helper.RateLimiter
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit

@Module
class HomeRateLimiterModule {
    @HomeScope
    @Provides
    fun provideHomeRateLimiter(): RateLimiter<String> = RateLimiter<String>(timeout = 3, timeUnit = TimeUnit.MINUTES)
}
