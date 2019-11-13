package com.tokopedia.promotionstarget.data.di.components

import android.content.Context
import com.tokopedia.promotionstarget.data.di.scopes.PromoTargetScope
import dagger.Module
import dagger.Provides

@PromoTargetScope
@Module
class AppModule(val applicationContext: Context) {

    @Provides
    @PromoTargetScope
    fun provideContext() = applicationContext
}