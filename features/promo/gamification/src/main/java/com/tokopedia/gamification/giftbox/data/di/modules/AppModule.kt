package com.tokopedia.gamification.giftbox.data.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application) {

    @Provides
    fun provideApplication(): Application = application

}