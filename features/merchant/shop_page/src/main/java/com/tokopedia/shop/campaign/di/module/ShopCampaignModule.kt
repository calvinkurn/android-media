package com.tokopedia.shop.campaign.di.module

import android.content.Context
import com.tokopedia.shop.campaign.di.scope.ShopCampaignScope
import com.tokopedia.shop.common.di.ShopPageContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopCampaignViewModelModule::class])
class ShopCampaignModule {
    @ShopCampaignScope
    @Provides
    fun provideUserSessionInterface(
        @ShopPageContext context: Context
    ): UserSessionInterface = UserSession(context)
}
