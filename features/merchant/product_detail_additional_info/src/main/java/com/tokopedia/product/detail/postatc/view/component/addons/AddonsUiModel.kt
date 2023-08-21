package com.tokopedia.product.detail.postatc.view.component.addons

import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import kotlin.math.roundToLong

data class AddonsUiModel(
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder(),
    var data: Data? = null
) : PostAtcUiModel {

    override val id: Int = hashCode()

    override fun equalsWith(newItem: PostAtcUiModel) =
        newItem is AddonsUiModel

    override fun newInstance(): PostAtcUiModel = this.copy()

    data class Data(
        val cartId: String,
        val title: String,
        val productId: String,
        val warehouseId: String,
        val isFulfillment: Boolean,
        val selectedAddonsIds: List<String>,
        val deselectedAddonsIds: List<String>,
        val categoryId: String,
        val shopId: String,
        val quantity: Long,
        val price: Double,
        val discountedPrice: Double,
        val condition: String
    ) {
        val addonsWidgetParam: AddOnParam
            get() = AddOnParam(
                productId = productId,
                warehouseId = warehouseId,
                isTokocabang = isFulfillment,
                categoryID = categoryId,
                shopID = shopId,
                quantity = quantity,
                price = price.roundToLong(),
                discountedPrice = discountedPrice.roundToLong(),
                condition = condition
            )
    }
}
