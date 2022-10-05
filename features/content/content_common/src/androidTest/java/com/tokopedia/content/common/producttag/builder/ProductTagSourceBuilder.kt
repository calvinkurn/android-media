package com.tokopedia.content.common.producttag.builder

import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
class ProductTagSourceBuilder {

    fun buildComplete(): String {
        return listOf(
            ProductTagSource.GlobalSearch,
            ProductTagSource.MyShop,
            ProductTagSource.LastPurchase,
        ).joinToString(separator = ",") { it.tag }
    }
}