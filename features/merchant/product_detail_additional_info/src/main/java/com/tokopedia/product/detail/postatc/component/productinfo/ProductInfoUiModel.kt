package com.tokopedia.product.detail.postatc.component.productinfo

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

data class ProductInfoUiModel(
    val title: String,
    val subtitle: String,
    val imageLink: String,
    val buttonText: String,
    val cartId: String,
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder()
) : PostAtcUiModel {
    override val id = hashCode()
    override fun equalsWith(newItem: PostAtcUiModel): Boolean {
        return newItem is ProductInfoUiModel &&
            title == newItem.title &&
            subtitle == newItem.subtitle &&
            imageLink == newItem.imageLink &&
            buttonText == newItem.buttonText &&
            cartId == newItem.cartId
    }
}
