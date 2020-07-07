package com.tokopedia.product.manage.feature.campaignstock.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.fragment.CampaignMainStockFragment
import dagger.Component

@CampaignStockScope
@Component(
        modules = [CampaignStockModule::class],
        dependencies = [ProductManageComponent::class])
interface CampaignStockComponent {

    fun inject(campaignMainStockFragment: CampaignMainStockFragment)
}