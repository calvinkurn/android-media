package com.tokopedia.promotionstarget.data.di.modules

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideApplication(): Application {
        if (context is Application)
            return context
        else if (context is Activity) {
            return context.application
        } else throw IllegalArgumentException("Provide either application or activity")
    }

    @Provides
    fun provideContext(): Context {
        return context
    }

    //todo Rahul remove after testing
    @Provides
    fun providerGratifSharedPrefs(): SharedPreferences = context.getSharedPreferences("promo_gratif", Context.MODE_PRIVATE)


}