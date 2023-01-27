package com.tokopedia.product_ar.util

import android.graphics.Color
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductAr
import org.junit.Assert

class ProductArAssertAssistant {

    fun assertSelectedArData(data: ProductAr,
                             expectedProductId: String,
                             expectedName: String,
                             expectedPrice: Double,
                             expectedUnavailableCopy: String,
                             expectedButtonText: String,
                             expectedButtonType: String,
                             expectedButtonColor: String,
                             expectedCampaignActive: Boolean
    ) {
        Assert.assertEquals(data.name, expectedName)
        Assert.assertEquals(data.productID, expectedProductId)
        Assert.assertEquals(data.price.toFloat(), expectedPrice.toFloat())
        Assert.assertEquals(data.unavailableCopy, expectedUnavailableCopy)
        Assert.assertEquals(data.button.text, expectedButtonText)
        Assert.assertEquals(data.button.cartType, expectedButtonType)
        Assert.assertEquals(data.button.color, expectedButtonColor)
        Assert.assertEquals(data.campaignInfo.isActive, expectedCampaignActive)
    }

    fun assertSelectedModifaceLook(data: MFEMakeupLook, expectedColor: Int) {
        Assert.assertEquals(data.lipLayers.firstOrNull()?.product?.color ?: 0,
                expectedColor)
    }

    fun assertSelectedModifaceUiModel(
            data: List<ModifaceUiModel>,
            expectedProductId: String,
            expectedBackgroundUrl: String,
            expectedProductName: String,
            expectedModifaceType: String
    ) {
        val selectedUiData = data.firstOrNull { it.isSelected }

        Assert.assertNotNull(selectedUiData)
        Assert.assertEquals(selectedUiData!!.isSelected, true)
        Assert.assertEquals(selectedUiData.productId, expectedProductId)
        Assert.assertEquals(selectedUiData.backgroundUrl, expectedBackgroundUrl)
        Assert.assertEquals(selectedUiData.productName, expectedProductName)
        Assert.assertEquals(selectedUiData.modifaceType, expectedModifaceType)
    }

    fun assertMakeUpLook(list: List<ModifaceUiModel>) {
        list.forEachIndexed { index, modifaceUiModel ->
            val makeupLook = modifaceUiModel.modifaceProductData

            when (index) {
                0 -> {
                    val colorRgb = Color.argb(255, 110, 49, 44)
                    assertEmptyData(makeupLook, colorRgb)
                }

                2 -> {
                    val colorRgb = Color.argb(255, 49, 55, 115)
                    assertEmptyData(makeupLook, colorRgb)
                }
                3 -> {
                    val colorRgb = Color.argb(255, 104, 3, 12)
                    assertEmptyData(makeupLook, colorRgb)
                }
                4 -> {
                    val colorRgb = Color.argb(255, 144, 39, 23)
                    assertEmptyData(makeupLook, colorRgb)
                }
            }
        }
    }

    private fun assertEmptyData(data: MFEMakeupLook, color: Int) {
        val product = data.lipLayers.firstOrNull()?.product ?: MFEMakeupProduct()

        Assert.assertEquals(product.color, color)

        Assert.assertEquals(product.gloss, 0.0F)
        Assert.assertEquals(product.glossDetail, 0.0F)
        Assert.assertEquals(product.wetness, 0.0F)
        Assert.assertEquals(product.glitter, 0.0F)

        val glitterColorRgb = Color.rgb(0, 0, 0)
        Assert.assertEquals(product.glitterColor, glitterColorRgb)
        Assert.assertEquals(product.matteness, 0.0F)
        Assert.assertEquals(product.glitterDensity, 1F) //sparkleDensity default value
        Assert.assertEquals(product.glitterSize, 1F) //sparkleSize default value
        Assert.assertEquals(product.glitterColorVariation, 0F)
        Assert.assertEquals(product.glitterSizeVariation, 0F)
        Assert.assertEquals(product.glitterBaseReflectivity, 0.3F) //sparkleBaseReflectivity default value
        Assert.assertEquals(product.envMappingIntensity, 0F) //sparkleBaseReflectivity default value
        val envMappingColorRgb = Color.rgb(0, 0, 0)
        Assert.assertEquals(product.envMappingColor, envMappingColorRgb)
        Assert.assertEquals(product.envMappingBumpIntensity, 0.6F) //envMappingBumpIntensity default value
        Assert.assertEquals(product.envMappingCurve, 2.3F) //envMappingCurve default value
        Assert.assertEquals(product.envMappingRotationY, 0F) //envMappingCurve default value
        Assert.assertEquals(product.metallicIntensity, 0F) //envMappingCurve default value
        Assert.assertEquals(product.vinylIntensity, 0F) //envMappingCurve default value
        Assert.assertEquals(product.amount, 1F) //amount default value

        Assert.assertEquals(data.lipVolume, 0F) //amount default value
    }

    private fun assertFullyProvidedData(data: MFEMakeupLook) {
        val product = data.lipLayers.firstOrNull()?.product ?: MFEMakeupProduct()
        val colorRgb = Color.argb(255, 115, 55, 49)

        Assert.assertEquals(product.color, colorRgb)

        Assert.assertEquals(product.gloss, 1F)
        Assert.assertEquals(product.glossDetail, 1F)
        Assert.assertEquals(product.wetness, 1F)
        Assert.assertEquals(product.glitter, 1F)

        val glitterColorRgb = Color.rgb(255, 210, 230)
        Assert.assertEquals(product.glitterColor, glitterColorRgb)
        Assert.assertEquals(product.matteness, 1F)
        Assert.assertEquals(product.glitterDensity, 1F)
        Assert.assertEquals(product.glitterSize, 2.04F)
        Assert.assertEquals(product.glitterColorVariation, 1F)
        Assert.assertEquals(product.glitterSizeVariation, 1F)
        Assert.assertEquals(product.glitterBaseReflectivity, 1F)
        Assert.assertEquals(product.envMappingIntensity, 1F)
        val envMappingColorRgb = Color.rgb(255, 255, 255)
        Assert.assertEquals(product.envMappingColor, envMappingColorRgb)
        Assert.assertEquals(product.envMappingBumpIntensity, 0.6F)
        Assert.assertEquals(product.envMappingCurve, 2.3F)
        Assert.assertEquals(product.envMappingRotationY, 1F)
        Assert.assertEquals(product.metallicIntensity, 0F)
        Assert.assertEquals(product.vinylIntensity, 0F)
        Assert.assertEquals(product.amount, 1F)

        Assert.assertEquals(data.lipVolume, 0.2F)
    }
}