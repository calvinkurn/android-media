package com.tokopedia.product_ar.util

import android.graphics.Color
import com.google.gson.Gson
import com.modiface.mfemakeupkit.effects.MFEMakeupLipLayer
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductArResponse
import com.tokopedia.product_ar.model.ProductArUiModel

object ProductArUseCaseMapperTest {

    fun mapToModifaceUiModel(initialProductId: String, data: ProductArUiModel): List<ModifaceUiModel> {
        var count = -1
        return data.options.map {
            count++
            val isSelected = initialProductId == it.value.productID
            ModifaceUiModel(
                    modifaceProductData = convertToMfeMakeUpLook(
                            it.value.providerDataCompiled) ?: MFEMakeupLook(),
                    isSelected = isSelected,
                    backgroundUrl = data.optionBgImage,
                    productName = it.value.name,
                    productId = it.value.productID,
                    modifaceType = it.value.type
            )
        }
    }

    fun mapIntoUiModel(response: ProductArResponse, gson: Gson): ProductArUiModel {
        return ProductArUiModel(
                optionBgImage = response.data.optionBgImage,
                provider = response.data.provider,
                options = response.data.productArs.associateBy(
                        {
                            it.productID
                        }, {
                    it.copy(providerDataCompiled = convertToObject(gson, it.providerData))
                })
        )
    }

    private fun convertToObject(gson: Gson, modifaceRawJson: String): ModifaceProvider {
        return gson.fromJson(modifaceRawJson, Array<ModifaceProvider>::class.java).firstOrNull()
                ?: ModifaceProvider()
    }


    private fun convertToMfeMakeUpLook(data: ModifaceProvider?): MFEMakeupLook? {
        if (data == null) return null

        val color = Color.argb(data.colorA.toIntSafely(),
                data.colorR.toIntSafely(),
                data.colorG.toIntSafely(),
                data.colorB.toIntSafely())

        val product = MFEMakeupProduct().also {
            it.gloss = data.getGlossFormula()
            it.glossDetail = data.getGlossDetailFormula()
            it.wetness = data.getWetnessFormula()
            it.glitter = data.getSparkleAFormula()
            it.glitterColor = data.getGlitterColor()
            it.color = color
            it.matteness = data.matteness
            it.glitterDensity = data.getGlitterDensityFormula()
            it.glitterSize = data.getGlitterSizeFormula()
            it.glitterColorVariation = data.getGlitterColorVariationFormula()
            it.glitterSizeVariation = data.getGlitterSizeVariationFormula()
            it.glitterBaseReflectivity = data.getGlitterBaseReflectivityFormula()
            it.envMappingIntensity = data.getEnvMappingtFormula()
            it.envMappingColor = data.getEnvMappingColor()
            it.envMappingBumpIntensity = data.getEnvBumpDensityFormula()
            it.envMappingCurve = data.getEnvMappingCurveFormula()
            it.envMappingRotationY = data.envMappingRotationY
            it.metallicIntensity = data.metallicIntensity
            it.vinylIntensity = data.vinylIntensity
            it.amount = data.getAmountFormula()
        }

        MFEMakeupLook().apply {
            lipVolume = data.lipPlumping
            lipLayers.add(MFEMakeupLipLayer(product))
        }.let {
            return it
        }
    }
}