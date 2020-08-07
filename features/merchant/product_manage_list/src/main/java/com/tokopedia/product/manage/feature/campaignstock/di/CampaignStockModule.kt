package com.tokopedia.product.manage.feature.campaignstock.di

import dagger.Module

@CampaignStockScope
@Module(includes = [CampaignStockViewModelModule::class])
class CampaignStockModule