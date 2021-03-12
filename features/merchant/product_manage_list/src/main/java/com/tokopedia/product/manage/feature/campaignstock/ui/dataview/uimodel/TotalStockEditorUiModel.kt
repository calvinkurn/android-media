package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory

class TotalStockEditorUiModel(
    val totalStock: Int,
    val isCampaign: Boolean?,
    val access: ProductManageAccess?
): Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)

    fun isEmpty(): Boolean {
        return totalStock == 0
    }
}