package com.tokopedia.play.view.uimodel

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory

/**
 * Created by jegul on 06/03/20
 */
class VariantSheetUiModel(
        var product: PlayProductUiModel.Product,
        val action: ProductAction,
        val parentVariant: ProductVariant? = null,
        var stockWording: String? = null,
        var listOfVariantCategory: List<VariantCategory> = listOf(),
        var mapOfSelectedVariants: MutableMap<String, String> = mutableMapOf()
) {
    fun isPartialySelected(): Boolean = mapOfSelectedVariants.any {
        it.value.toLongOrZero() == 0L
    } || mapOfSelectedVariants.isEmpty()
}