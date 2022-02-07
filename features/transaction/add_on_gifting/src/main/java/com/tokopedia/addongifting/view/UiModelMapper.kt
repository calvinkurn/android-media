package com.tokopedia.addongifting.view

import com.tokopedia.addongifting.data.response.AddOnDataResponse
import com.tokopedia.addongifting.data.response.GetAddOnByProductResponse
import com.tokopedia.addongifting.data.response.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import kotlin.math.roundToLong

object UiModelMapper {

    fun mapProduct(addOnProductData: AddOnProductData, getAddOnByProductResponse: GetAddOnByProductResponse): ProductUiModel {
        val addOnByProduct = getAddOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        return ProductUiModel().apply {
            shopType = addOnByProduct?.addOns?.firstOrNull()?.shop?.shopType ?: 0
            shopName = addOnByProduct?.addOns?.firstOrNull()?.shop?.name ?: ""
            mainProductImageUrl = addOnProductData.products.firstOrNull()?.productImageUrl ?: ""
            mainProductName = addOnProductData.products.firstOrNull()?.productName ?: ""
            mainProductPrice = addOnProductData.products.firstOrNull()?.productPrice ?: 0
            otherProductCount = addOnProductData.products.size - 1
        }
    }

    fun mapAddOn(addOnProductData: AddOnProductData, addOnByProductResponse: GetAddOnByProductResponse, addOnSavedStateResponse: GetAddOnSavedStateResponse): AddOnUiModel {
        val addOnByProduct = addOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        val addOn = addOnByProduct?.addOns?.firstOrNull()
        return AddOnUiModel().apply {
            productCount = addOnProductData.products.size
            addOnName = addOn?.basicInfo?.name ?: ""
            addOnDescription = ""
            addOnPrice = addOn?.inventory?.price?.roundToLong() ?: 0
            addOnSquareImageUrl = addOn?.pictures?.firstOrNull()?.url100 ?: ""
            val imageUrls = mutableListOf<String>()
            addOn?.pictures?.forEach {
                imageUrls.add(it.url)
            }
            addOnAllImageUrls = imageUrls
            val addonSavedStateData = getAddOnSavedStateById(addOn?.basicInfo?.id
                    ?: "", addOnSavedStateResponse)
            addOnSelectedState = addonSavedStateData != null
            addOnNoteTo = addonSavedStateData?.addOnMetadata?.addOnNote?.to ?: ""
            addOnNoteFrom = addonSavedStateData?.addOnMetadata?.addOnNote?.from ?: ""
            addOnNote = addonSavedStateData?.addOnMetadata?.addOnNote?.notes ?: ""
            isCustomNote = addOn?.basicInfo?.rules?.customNote ?: false
            isLoadingNoteState = false
        }
    }

    private fun getAddOnSavedStateById(addOnId: String, addOnSavedStateResponse: GetAddOnSavedStateResponse): AddOnDataResponse? {
        if (addOnId.isNotBlankOrZero()) {
            addOnSavedStateResponse.addOns.forEach {
                it.addOnData.forEach {
                    if (it.addOnId == addOnId) {
                        return it
                    }
                }
            }
        }

        return null
    }

}