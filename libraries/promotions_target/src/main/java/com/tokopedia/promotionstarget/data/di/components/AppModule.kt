package com.tokopedia.promotionstarget.data.di.components

import android.app.Application
import android.content.Context
import com.tokopedia.promotionstarget.data.di.scopes.PromoTargetScope
import dagger.Module
import dagger.Provides

@PromoTargetScope
@Module
class AppModule(val application: Application) {

    @Provides
    @PromoTargetScope
    fun provideApplication() = application

    @Provides
    @PromoTargetScope
    fun provideContext():Context = application.applicationContext

}