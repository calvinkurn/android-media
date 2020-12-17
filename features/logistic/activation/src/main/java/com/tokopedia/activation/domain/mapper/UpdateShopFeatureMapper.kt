package com.tokopedia.activation.domain.mapper

import com.tokopedia.activation.model.UpdateFeatureModel
import com.tokopedia.activation.model.response.UpdateShopFeature

object UpdateShopFeatureMapper {

    fun convertToUIModel(data: UpdateShopFeature): UpdateFeatureModel {
        return UpdateFeatureModel().apply {
            success = data.success
            message = data.message
        }
    }
}