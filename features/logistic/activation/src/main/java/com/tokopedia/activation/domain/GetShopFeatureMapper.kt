package com.tokopedia.activation.domain

import com.tokopedia.activation.model.ShopData
import com.tokopedia.activation.model.ShopFeatureModel

object GetShopFeatureMapper {

    fun convertToUIModel(data: ShopData): ShopFeatureModel {
        return ShopFeatureModel().apply {
            title = data.title
            type = data.type
            value = data.value
        }
    }
}