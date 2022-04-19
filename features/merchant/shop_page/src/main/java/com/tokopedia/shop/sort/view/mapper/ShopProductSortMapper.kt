package com.tokopedia.shop.sort.view.mapper

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import java.util.*

/**
 * Created by nathan on 2/25/18.
 */
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