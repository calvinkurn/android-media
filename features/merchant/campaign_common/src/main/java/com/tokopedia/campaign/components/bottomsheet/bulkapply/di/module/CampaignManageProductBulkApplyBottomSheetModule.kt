package com.tokopedia.campaign.components.bottomsheet.bulkapply.di.module

import com.tokopedia.campaign.components.bottomsheet.bulkapply.di.scope.CampaignManageProductBulkApplyBottomSheetScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CampaignManageProductBulkApplyBottomSheetModule {

    @CampaignManageProductBulkApplyBottomSheetScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}