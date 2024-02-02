package com.tokopedia.order_management_common.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.order_management_common.presentation.factory.AddOnAdapterFactory

data class AddOnSummaryUiModel(
    val addOnIdentifier: String,
    val totalPriceText: StringRes,
    val addonsLogoUrl: String,
    val addonsTitle: String,
    val addonItemList: List<AddonItemUiModel>,
    val canExpandCollapse: Boolean
) {
    var isExpand: Boolean = true

    data class AddonItemUiModel(
        val priceText: String,
        val quantity: Int,
        val addonsId: String,
        val addOnsName: String,
        val type: String = String.EMPTY,
        val addOnsThumbnailUrl: String,
        val toStr: String,
        val fromStr: String,
        val message: String,
        var descriptionExpanded: Boolean = false,
        val noteCopyable: Boolean,
        val providedByShopItself: Boolean,
        val infoLink: String,
        val tips: String,
        //region Product Benefit Data
        val orderId: String,
        val orderDetailId: String
        //endregion Product Benefit Data
    ) : Visitable<AddOnAdapterFactory> {

        override fun type(typeFactory: AddOnAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}
