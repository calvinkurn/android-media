package com.tokopedia.shop_showcase.shop_showcase_add.domain.mapper

import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AppendedProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.BaseShowcaseProduct
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct

class AppendedProductMapper  {

    fun mapToGqlModel(appendedProductList: ArrayList<BaseShowcaseProduct>, showcaseId: String?): List<AppendedProduct> {
        return appendedProductList.map {
            val item = it as ShowcaseProduct
            AppendedProduct(
                    product_id = item.productId,
                    menu_id = showcaseId
            )
        }
    }

}