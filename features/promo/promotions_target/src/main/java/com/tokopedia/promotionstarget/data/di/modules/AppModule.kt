package com.tokopedia.promotionstarget.data.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application) {

    @Provides
    fun provideApplication() = application

    @Provides
    fun provideContext(): Context = application.applicationContext

}