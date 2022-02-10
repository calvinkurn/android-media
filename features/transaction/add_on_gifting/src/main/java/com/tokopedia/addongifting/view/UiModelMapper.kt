package com.tokopedia.addongifting.view

import com.tokopedia.addongifting.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.data.getaddonsavedstate.AddOnDataResponse
import com.tokopedia.addongifting.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.ProductUiModel
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnDataResult
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import kotlin.math.roundToLong

object UiModelMapper {

    fun mapProduct(addOnProductData: AddOnProductData, getAddOnByProductResponse: GetAddOnByProductResponse): ProductUiModel {
        val addOnByProduct = getAddOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        return ProductUiModel().apply {
            isTokoCabang = addOnProductData.isTokoCabang
            shopBadgeUrl = addOnProductData.shopBadgeUrl
            shopName = addOnByProduct?.addOns?.firstOrNull()?.shop?.name ?: ""
            mainProductImageUrl = addOnProductData.products.firstOrNull()?.productImageUrl ?: ""
            mainProductName = addOnProductData.products.firstOrNull()?.productName ?: ""
            mainProductPrice = addOnProductData.products.firstOrNull()?.productPrice ?: 0
            otherProductCount = addOnProductData.products.size - 1
        }
    }

    fun mapAddOn(addOnProductData: AddOnProductData, addOnByProductResponse: GetAddOnByProductResponse, addOnSavedStateResponse: GetAddOnSavedStateResponse? = null): AddOnUiModel {
        val addOnByProduct = addOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        val addOn = addOnByProduct?.addOns?.firstOrNull()
        return AddOnUiModel().apply {
            isTokoCabang = addOnProductData.isTokoCabang
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
            if (addOnSavedStateResponse != null) {
                // Get saved state from API
                val addonSavedStateData = getAddOnSavedStateById(addOn?.basicInfo?.id
                        ?: "", addOnSavedStateResponse)
                isAddOnSelected = addonSavedStateData != null
                initialAddOnNoteTo = addonSavedStateData?.addOnMetadata?.addOnNote?.to ?: ""
                addOnNoteTo = initialAddOnNoteTo
                initialAddOnNoteFrom = addonSavedStateData?.addOnMetadata?.addOnNote?.from ?: ""
                addOnNoteFrom = initialAddOnNoteFrom
                initialAddOnNote = addonSavedStateData?.addOnMetadata?.addOnNote?.notes ?: ""
                addOnNote = initialAddOnNote
            } else {
                // Get saved state from previous page (Checkout / OSP)
                val addonSavedStateData = getAddOnSavedStateById(addOn?.basicInfo?.id
                        ?: "", addOnProductData.addOnSavedStates)
                isAddOnSelected = addonSavedStateData != null
                initialAddOnNoteTo = addonSavedStateData?.addOnMetadata?.addOnNote?.to ?: ""
                addOnNoteTo = initialAddOnNoteTo
                initialAddOnNoteFrom = addonSavedStateData?.addOnMetadata?.addOnNote?.from ?: ""
                addOnNoteFrom = initialAddOnNoteFrom
                initialAddOnNote = addonSavedStateData?.addOnMetadata?.addOnNote?.notes ?: ""
                addOnNote = initialAddOnNote
            }
            isCustomNote = addOn?.basicInfo?.rules?.customNote ?: false
            addOnFooterMessages = addOnProductData.addOnFooterMessages
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

    private fun getAddOnSavedStateById(addOnId: String, addOnSavedStateResults: List<AddOnDataResult>): AddOnDataResult? {
        if (addOnId.isNotBlankOrZero()) {
            addOnSavedStateResults.forEach {
                if (it.addOnId == addOnId) {
                    return it
                }
            }
        }

        return null
    }

}