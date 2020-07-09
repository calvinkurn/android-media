package com.tokopedia.product.manage.feature.campaignstock.ui.dataview

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellableStockProductUIModel(
        val productId: String,
        val warehouseId: String,
        val productName: String,
        val stock: String): Parcelable, Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)
}