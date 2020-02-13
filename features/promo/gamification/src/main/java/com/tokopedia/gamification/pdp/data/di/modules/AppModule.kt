package com.tokopedia.gamification.pdp.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gamification.pdp.data.di.scopes.GamificationPdpScope
import dagger.Module
import dagger.Provides

@GamificationPdpScope
@Module
class AppModule(val applicationContext: Context) {

    @Provides
    @GamificationPdpScope
    @ApplicationContext
    fun provideContext() = applicationContext
}