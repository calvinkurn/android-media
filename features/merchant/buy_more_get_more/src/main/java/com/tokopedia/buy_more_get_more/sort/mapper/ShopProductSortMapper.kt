package com.tokopedia.buy_more_get_more.sort.mapper

import com.tokopedia.buy_more_get_more.sort.model.ShopProductSortModel
import com.tokopedia.shop.common.graphql.data.shopsort.ShopProductSort


class ShopProductSortMapper {
    fun convertSort(shopProductSortList: List<ShopProductSort?>): List<ShopProductSortModel> {
        val result: MutableList<ShopProductSortModel> = ArrayList()
        for (data in shopProductSortList) {
            val shopProductFilterModel = ShopProductSortModel()
            shopProductFilterModel.inputType = data?.inputType.orEmpty()
            shopProductFilterModel.key = data?.key.orEmpty()
            shopProductFilterModel.name = data?.name.orEmpty()
            shopProductFilterModel.value = data?.value.orEmpty()
            result.add(shopProductFilterModel)
        }
        return result
    }
}
