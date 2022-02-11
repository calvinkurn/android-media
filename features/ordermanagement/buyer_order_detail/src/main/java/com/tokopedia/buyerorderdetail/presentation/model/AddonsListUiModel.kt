package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory

data class AddonsListUiModel(
    val totalPriceText: String,
    val addonsLogoUrl: String,
    val addonsTitle: String,
    val addonsItemList: List<AddonItemUiModel>,
) : Visitable<BuyerOrderDetailTypeFactory> {

    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class AddonItemUiModel(
        val priceText: String,
        val quantity: Int,
        val addonsId: String,
        val addOnsName: String,
        val addOnsThumbnailUrl: String,
        val isCustomNote: Boolean,
        val toStr: String,
        val fromStr: String,
        val message: String
    )
}