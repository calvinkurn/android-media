package com.tokopedia.promotionstarget.data.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.mockk.mockk

@Module
class TestAppModule(val context: Context) {
    @Provides
    fun provideApplication(): Application {
        return mockk()
    }

    @Provides
    fun provideContext(): Context {
        return context
    }
}