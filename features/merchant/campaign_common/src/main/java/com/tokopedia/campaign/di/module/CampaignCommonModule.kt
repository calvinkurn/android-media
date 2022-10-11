package com.tokopedia.campaign.di.module

import com.tokopedia.campaign.di.scope.CampaignCommonScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CampaignCommonModule {

    @CampaignCommonScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
