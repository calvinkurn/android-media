package com.tokopedia.activation.domain.mapper

import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.response.ShopData

object GetShopFeatureMapper {

    fun convertToUIModel(data: ShopData): ShopFeatureModel {
        return ShopFeatureModel().apply {
            title = data.title
            type = data.type
            value = data.value
        }
    }
}