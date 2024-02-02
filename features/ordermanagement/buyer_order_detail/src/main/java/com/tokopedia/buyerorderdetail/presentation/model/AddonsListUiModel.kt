package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.order_management_common.presentation.uimodel.StringRes

data class AddonsListUiModel(
    val addOnIdentifier: String,
    val totalPriceText: StringRes?,
    val addonsLogoUrl: String,
    val addonsTitle: String,
    val addonsItemList: List<AddonItemUiModel>,
    val canExpandCollapse: Boolean,
    val showTotalPrice: Boolean
) : Visitable<BuyerOrderDetailTypeFactory> {

    var isExpand: Boolean = true

    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class AddonItemUiModel(
        val priceText: String,
        val quantity: Int,
        val addonsId: String,
        val addOnsName: String,
        val type: String,
        val addOnsThumbnailUrl: String,
        val toStr: String,
        val fromStr: String,
        val message: String,
        val providedByShopItself: Boolean,
        val infoLink: String,
        val tips: String
    )
}
