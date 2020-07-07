package com.tokopedia.product.manage.feature.campaignstock.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import dagger.Component

@CampaignStockScope
@Component(
        modules = [CampaignStockModule::class],
        dependencies = [ProductManageComponent::class])
interface CampaignStockComponent {

}