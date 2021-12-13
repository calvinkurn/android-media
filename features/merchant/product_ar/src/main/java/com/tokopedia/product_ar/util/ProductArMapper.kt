package com.tokopedia.product_ar.util

import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductArUiModel

object ProductArMapper {

    fun mapToModifaceUiModel(initialProductId: String, data: ProductArUiModel): List<ModifaceUiModel> {
        return data.options.map {
            val isSelected = initialProductId == it.value.productID
            ModifaceUiModel(
                    modifaceProvider = it.value.providerDataCompiled ?: ModifaceProvider(),
                    isSelected = isSelected,
                    backgroundUrl = data.optionBgImage,
                    productName = it.value.name,
                    productId = it.value.productID
            )
        }
    }

    fun updateListAfterChangeSelection(productId: String, data: List<ModifaceUiModel>): List<ModifaceUiModel> {
        return data.map {
            if (it.productId == productId) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
    }
}