package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class VariantReservedEventInfoUiModel(
    val variantId: String,
    val variantName: String,
    val totalCampaign: Int,
    val totalStock: Int,
    val reservedEventInfos: MutableList<ReservedEventInfoUiModel>
): Parcelable, Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
        typeFactory.type(this)

}