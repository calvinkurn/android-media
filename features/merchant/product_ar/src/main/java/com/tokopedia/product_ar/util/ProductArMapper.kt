package com.tokopedia.product_ar.util

import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductArUiModel

object ProductArMapper {

    fun mapToModifaceUiModel(data: ProductArUiModel): List<ModifaceUiModel> {
        return data.options.map {
            ModifaceUiModel(
                    modifaceProvider = it.value.providerDataCompiled ?: ModifaceProvider(),
                    isSelected = false,
                    backgroundUrl = data.optionBgImage,
                    productName = it.value.name,
                    productId = it.value.productID
            )
        }
    }
}