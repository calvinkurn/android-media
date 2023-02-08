package com.tokopedia.product.detail.postatc.component.productinfo

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel

class ProductInfoUiModel(
    val title: String,
    val subtitle: String,
    val imageLink: String,
    val buttonText: String,
    val cartId: String,
    override val name: String,
    override val type: String,
    override val impressHolder: ImpressHolder = ImpressHolder()
) : PostAtcUiModel{
    override val id = hashCode()
}
