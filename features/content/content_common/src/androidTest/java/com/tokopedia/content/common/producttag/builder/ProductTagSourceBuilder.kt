package com.tokopedia.content.common.producttag.builder

import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
class ProductTagSourceBuilder {

    fun buildSourceList(takeOut: () -> List<ProductTagSource> = { listOf() }): String {
        return getAllSource().filterNot { takeOut().contains(it) }.joinToString(separator = ",") { it.tag }
    }

    private fun getAllSource() = listOf(
        ProductTagSource.GlobalSearch,
        ProductTagSource.LastPurchase,
        ProductTagSource.MyShop,
    )
}