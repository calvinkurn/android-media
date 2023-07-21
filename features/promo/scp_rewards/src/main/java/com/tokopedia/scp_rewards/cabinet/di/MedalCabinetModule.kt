package com.tokopedia.scp_rewards.cabinet.di

import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalytics
import com.tokopedia.scp_rewards.cabinet.analytics.MedalCabinetAnalyticsImpl
import dagger.Module
import dagger.Provides

@Module
class MedalCabinetModule {

    @Provides
    fun provideMedalCabinetAnalytics(): MedalCabinetAnalytics = MedalCabinetAnalyticsImpl
}
