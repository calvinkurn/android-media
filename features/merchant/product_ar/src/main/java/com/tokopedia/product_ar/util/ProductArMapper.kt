package com.tokopedia.product_ar.util

import android.graphics.Color
import com.modiface.mfemakeupkit.effects.MFEMakeupLipLayer
import com.modiface.mfemakeupkit.effects.MFEMakeupLook
import com.modiface.mfemakeupkit.effects.MFEMakeupProduct
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product_ar.model.ComparissonImageUiModel
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.model.ProductArUiModel
import com.tokopedia.product_ar.model.state.ImageMapMode

object ProductArMapper {

    fun generateAtcRequestParam(productAr: ProductAr?,
                                shopId: String,
                                userId: String): AddToCartRequestParams {
        return AddToCartRequestParams().apply {
            productId = productAr?.productID.toLongOrZero()
            quantity = productAr?.getFinalMinOrder() ?: 1
            notes = ""
            attribution = ""
            listTracker = ""
            productName = productAr?.name ?: ""
            price = productAr?.getFinalPrice().toString()
            this.shopId = shopId.toIntOrZero()
            this.userId = userId
        }
    }

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

    fun updateListAfterChangeSelection(productId: String, data: List<ModifaceUiModel>): List<ModifaceUiModel> {
        return data.map {
            if (it.productId == productId) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
    }

    fun getMfMakeUpLookByProductId(data: List<ModifaceUiModel>,
                                   selectedProductId: String): MFEMakeupLook {
        val selectedData = data.firstOrNull { it.productId == selectedProductId }
        return selectedData?.modifaceProductData ?: MFEMakeupLook()
    }

    fun needToDisableSelection(selectedProductId: String,
                               data: List<ModifaceUiModel>,
                               isSelected: Boolean,
                               currentCounter: Int): Boolean {
        val tryUnselectOneData = data.any {
            it.productId == selectedProductId
                    && it.isSelected
                    && it.counter == 1
                    && currentCounter == 1
        }
        val tryToSelectMoreThanFour = currentCounter + 1 > 4 && !isSelected

        return (tryUnselectOneData || tryToSelectMoreThanFour)
    }

    /**
     * Comparison Fragment Mapper
     */
    fun updateInitialListWithCounter(data: List<ModifaceUiModel>): List<ModifaceUiModel> {
        return data.map {
            if (it.isSelected) {
                it.copy(counter = 1)
            } else {
                it
            }
        }
    }

    fun decideSpanSize(listImages: List<ComparissonImageUiModel>,
                       mode: ImageMapMode): Int {
        return if (listImages.size > 1) {
            2
        } else {
            1
        }
    }

    fun decideRemovedPosition(isSelected: Boolean,
                              unUpdatedList: List<ModifaceUiModel>,
                              selectedProductId: String): Int {
        return if (isSelected) {
            (unUpdatedList.firstOrNull {
                it.productId == selectedProductId
            }?.counter ?: -1) - 1
        } else {
            -1
        }
    }

    fun getInitialSelectedProductName(source: List<ModifaceUiModel>): String {
        return source.firstOrNull {
            it.isSelected
        }?.productName ?: ""
    }

    fun updateListAfterSelectedCounter(data: List<ModifaceUiModel>,
                                       counterTotal: Int,
                                       selectedProductId: String,
                                       selectedListener: () -> Unit,
                                       unselectedListener: () -> Unit): List<ModifaceUiModel> {

        val unselectCounter = data.firstOrNull {
            //value if unselect
            it.isSelected && it.productId == selectedProductId
        }?.counter ?: -1

        return data.map {
            if (it.productId == selectedProductId) {
                if (it.isSelected) {
                    //unselect chip
                    unselectedListener.invoke()
                    it.copy(isSelected = false, counter = null)
                } else {
                    //select chip
                    selectedListener.invoke()
                    it.copy(isSelected = true, counter = counterTotal + 1)
                }
            } else {
                if (unselectCounter != -1) {
                    //means there is unselect somewhere else
                    if (it.counter ?: 1 > unselectCounter) {
                        /**
                         * Need to update other counter
                         * ex:total counter 3 -> unselect position 2 -> subtract counter at position 3
                         */
                        it.copy(counter = (it.counter ?: 1) - 1)
                    } else {
                        it
                    }
                } else {
                    it
                }
            }
        }
    }

    private fun convertToMfeMakeUpLook(data: ModifaceProvider?): MFEMakeupLook? {
        if (data == null) return null

        val color = Color.argb(data.colorA.toIntSafely(),
                data.colorR.toIntSafely(),
                data.colorG.toIntSafely(),
                data.colorB.toIntSafely())

        val product = MFEMakeupProduct().also {
            it.gloss = data.gamma
            it.glossDetail = data.glossDetail
            it.wetness = data.wetness
            it.glitter = data.sparkleA
            it.glitterColor = data.getGlitterColor()
            it.color = color
            it.matteness = data.matteness
            it.glitterDensity = data.sparkleDensity
            it.glitterSize = data.sparkleSize
            it.glitterColorVariation = data.sparkleColorVariation
            it.glitterSizeVariation = data.sparkleSizeVariation
            it.glitterBaseReflectivity = data.sparkleBaseReflectivity
            it.envMappingIntensity = data.envMappingIntensity
            it.envMappingColor = data.getEnvMappingColor()
            it.envMappingBumpIntensity = data.envMappingBumpIntensity
            it.envMappingCurve = data.envMappingCurve
            it.envMappingRotationY = data.envMappingRotationY
            it.metallicIntensity = data.metallicIntensity
            it.vinylIntensity = data.vinylIntensity
            it.amount = data.intensity
        }

        MFEMakeupLook().apply {
            lipVolume = data.lipPlumping
            lipLayers.add(MFEMakeupLipLayer(product))
        }.let {
            return it
        }
    }
}