package com.tokopedia.seller.menu.presentation.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.common.list.data.model.filter.Tab
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel

object SellerUiModelMapper {

    fun mapToProductUiModel(response: List<Tab>): ShopProductUiModel {
        var totalProductCount = 0

        response.map {
            totalProductCount += it.value.toIntOrZero()
        }

        return ShopProductUiModel(totalProductCount)
    }
}