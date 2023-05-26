package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseProductAdapter.TokoNowRepurchaseProductTypeFactory

data class TokoNowRepurchaseProductUiModel(
    val productId: String = "",
    val parentId: String = "",
    val shopId: String = "",
    val imageUrl: String = "",
    val minOrder: Int = 0,
    val maxOrder: Int = 0,
    val availableStock: Int = 0,
    val orderQuantity: Int = 0,
    val price: String = "",
    val formattedPrice: String = "",
    val discount: String = "",
    val discountInt: Int = 0,
    val slashPrice: String = "",
    val name: String = "",
    val rating: String = "",
    val progressBarLabel: String = "",
    val progressBarPercentage: Int = 0,
    val hasBeenWishlist: Boolean = false,
    val isSimilarProductShown: Boolean = false,
    val isWishlistShown: Boolean = false,
    val isVariant: Boolean = false,
    val needToShowQuantityEditor: Boolean = false,
    val labelGroupList: List<LabelGroup> = listOf(),
    val needToChangeMaxLinesName: Boolean = false,
    val originalPosition: Int = 0,
    val position: Int = 0,
    /**
     * Optional params
     */
    val channelId: String = "",
    val headerName: String = "",
    val categoryBreadcrumbs: String = ""
) : Visitable<TokoNowRepurchaseProductTypeFactory>, ImpressHolder() {

    companion object {
        internal const val LABEL_WEIGHT = "weight"
    }

    override fun type(typeFactory: TokoNowRepurchaseProductTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getChangePayload(newItem: TokoNowRepurchaseProductUiModel): Any {
        return orderQuantity != newItem.orderQuantity
    }

    fun getWeightLabelGroup(): LabelGroup? = labelGroupList.firstOrNull { it.isWeightPosition() }

    fun isOos() = availableStock < minOrder

    data class LabelGroup(
        val position: String = "",
        val title: String = "",
        val type: String = "",
        val imageUrl: String = ""
    ) {

        fun isWeightPosition() = position == LABEL_WEIGHT
    }
}
