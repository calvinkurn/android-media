package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellableStockProductUIModel(
        val productId: String,
        val productName: String,
        var stock: String,
        var isActive: Boolean,
        var isAllStockEmpty: Boolean,
        val access: ProductManageAccess,
        val isCampaign: Boolean
): Parcelable, Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)
}