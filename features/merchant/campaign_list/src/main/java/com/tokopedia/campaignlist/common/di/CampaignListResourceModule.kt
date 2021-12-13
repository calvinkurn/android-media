package com.tokopedia.campaignlist.common.di

import com.tokopedia.campaignlist.common.util.ResourceProvider
import com.tokopedia.campaignlist.common.util.ResourceProviderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CampaignListResourceModule {

    @Binds
    abstract fun bindResourceProvider(impl: ResourceProviderImpl): ResourceProvider
}