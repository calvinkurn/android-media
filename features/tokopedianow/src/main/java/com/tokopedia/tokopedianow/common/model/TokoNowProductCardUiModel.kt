package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.common.adapter.TokoNowProductCardAdapter.*
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType

data class TokoNowProductCardUiModel(
    val channelId: String,
    val productId: String,
    val shopId: String,
    val quantity: Int,
    var parentId: String,
    var product: ProductCardModel = ProductCardModel(),
    @TokoNowLayoutType val type: String,
    val position: Int,
    val headerName: String,
    val categoryBreadcrumbs: String = ""
): Visitable<TokoNowProductCardTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: TokoNowProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
