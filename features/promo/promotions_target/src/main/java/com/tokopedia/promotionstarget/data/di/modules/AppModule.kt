package com.tokopedia.promotionstarget.data.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class AppModule(val application: Application) {

    @Provides
    fun provideApplication() = application

    @Provides
    fun provideContext(): Context = application.applicationContext

    //todo Rahul remove after testing
    @Provides
    fun providerGratifSharedPrefs(): SharedPreferences = application.getSharedPreferences("promo_gratif", Context.MODE_PRIVATE)


}