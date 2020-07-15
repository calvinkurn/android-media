package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

interface CampaignStockListener {
    fun onTotalStockChanged(totalStock: Int)
    fun onActiveStockChanged(isActive: Boolean)
}