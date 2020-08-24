package com.tokopedia.product.manage.feature.campaignstock.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.fragment.CampaignStockFragment
import dagger.Component

@CampaignStockScope
@Component(
        modules = [CampaignStockModule::class],
        dependencies = [ProductManageComponent::class])
interface CampaignStockComponent {

    fun inject(campaignStockFragment: CampaignStockFragment)
}