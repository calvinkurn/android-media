package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(val applicationContext: Context) {

    @Provides
    @GamificationPdpScope
    fun provideContext() = applicationContext
}