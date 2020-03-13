package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductLineUiModel
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantCategory

/**
 * Created by jegul on 06/03/20
 */
data class VariantSheetUiModel(
        var product: ProductLineUiModel,
        val action: ProductAction,
        val parentVariant: ProductVariantCommon? = null,
        var stockWording: String?,
        var listOfVariantCategory: List<VariantCategory> = listOf(),
        var mapOfSelectedVariants: MutableMap<String, Int> = mutableMapOf()
) {
    fun isPartialySelected(): Boolean = mapOfSelectedVariants.any {
        it.value == 0
    }
}