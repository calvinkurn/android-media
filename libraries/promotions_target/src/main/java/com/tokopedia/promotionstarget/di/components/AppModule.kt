package com.tokopedia.promotionstarget.di.components

import android.content.Context
import com.tokopedia.promotionstarget.di.scopes.PromoTargetScope
import dagger.Module
import dagger.Provides

@PromoTargetScope
@Module
class AppModule(val applicationContext: Context) {

    @Provides
    @PromoTargetScope
    fun provideContext() = applicationContext
}