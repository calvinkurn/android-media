package com.tokopedia.product_ar.util

import android.graphics.Color
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductArUiModel

object ProductArMapper {

    fun mapToModifaceUiModel(initialProductId: String, data: ProductArUiModel): List<ModifaceUiModel> {
        return data.options.map {
            val isSelected = initialProductId == it.value.productID
            ModifaceUiModel(
                    modifaceProductData = convertToMfeMakeUpProduct(it.value.productID, it.value.providerDataCompiled)
                            ?: MFEMakeupProduct(),
                    isSelected = isSelected,
                    backgroundUrl = data.optionBgImage,
                    productName = it.value.name,
                    productId = it.value.productID,
                    modifaceType = it.value.type
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

    private fun convertToMfeMakeUpProduct(productId: String, data: ModifaceProvider?): MFEMakeupProduct? {
        if (data == null) return null
        val color = if(productId.toInt() % 2 == 0) {
            Color.argb(255, 222, 119, 133)
        } else {
            Color.argb(255, 109, 37, 39)
        }
        return MFEMakeupProduct().also {
//            it.color = Color.argb(data.colorA.toInt(),
//                    data.colorR.toInt(),
//                    data.colorG.toInt(),
//                    data.colorB.toInt())
            it.color = Color.argb(255, 66, 14, 18)
//            it.vinylIntensity = data.vinylIntensity
//            it.glossDetail = data.glossDetail
//            it.metallicIntensity = data.metallicIntensity
            it.gloss = 4.0f
            it.wetness = 1.0f
            it.amount = 1.0F
        }
    }
}