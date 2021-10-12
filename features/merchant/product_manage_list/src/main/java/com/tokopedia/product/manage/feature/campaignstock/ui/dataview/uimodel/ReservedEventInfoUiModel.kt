package com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.typefactory.CampaignStockTypeFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReservedEventInfoUiModel(
        val eventType: String,
        val eventName: String,
        val eventDesc: String,
        val stock: String,
        val actionWording: String,
        val actionUrl: String,
        val products: ArrayList<ReservedStockProductModel>,
        var isAccordionOpened: Boolean = false,
        var isVariant: Boolean = false,
        var isLastEvent: Boolean = false): Parcelable, Visitable<CampaignStockTypeFactory> {

    override fun type(typeFactory: CampaignStockTypeFactory): Int =
            typeFactory.type(this)
}