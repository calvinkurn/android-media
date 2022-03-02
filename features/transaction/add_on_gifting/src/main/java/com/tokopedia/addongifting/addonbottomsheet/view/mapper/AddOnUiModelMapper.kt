package com.tokopedia.addongifting.addonbottomsheet.view.mapper

import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.AddOnDataResponse
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateResponse
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.ProductUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import kotlin.math.roundToLong

object AddOnUiModelMapper {

    fun mapProduct(addOnProductData: AddOnProductData, getAddOnByProductResponse: GetAddOnByProductResponse): ProductUiModel {
        val addOnByProduct = getAddOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        val productData = addOnProductData.availableBottomSheetData.products.firstOrNull()
        return ProductUiModel().apply {
            isTokoCabang = addOnProductData.availableBottomSheetData.isTokoCabang
            shopName = addOnProductData.availableBottomSheetData.shopName
            mainProductImageUrl = productData?.productImageUrl ?: ""
            mainProductName = productData?.productName ?: ""
            mainProductQuantity = productData?.productQuantity ?: 0
            mainProductPrice = productData?.productPrice ?: 0
            otherProductCount = addOnProductData.availableBottomSheetData.products.size - 1
            promoMessage = getAddOnByProductResponse.dataResponse.staticInfo.promoText
        }
    }

    fun mapAddOn(addOnProductData: AddOnProductData, addOnByProductResponse: GetAddOnByProductResponse, addOnSavedStateResponse: GetAddOnSavedStateResponse? = null): AddOnUiModel {
        val addOnByProduct = addOnByProductResponse.dataResponse.addOnByProducts.firstOrNull()
        val addOn = addOnByProduct?.addOns?.firstOrNull()
        return AddOnUiModel().apply {
            isTokoCabang = addOnProductData.availableBottomSheetData.isTokoCabang
            productCount = addOnProductData.availableBottomSheetData.products.size
            mainProductQuantity = addOnProductData.availableBottomSheetData.products.firstOrNull()?.productQuantity ?: 0
            addOnId = addOn?.basicInfo?.id ?: ""
            addOnName = addOn?.basicInfo?.name ?: ""
            addOnType = addOn?.basicInfo?.productAddOnType ?: ""
            addOnDescription = ""
            addOnPrice = addOn?.inventory?.price?.roundToLong() ?: 0
            addOnQty = 1
            addOnSquareImageUrl = addOn?.basicInfo?.metadata?.pictures?.firstOrNull()?.url100 ?: ""
            val imageUrls = mutableListOf<String>()
            addOn?.basicInfo?.metadata?.pictures?.forEach {
                imageUrls.add(it.url)
            }
            addOnAllImageUrls = imageUrls
            if (addOnSavedStateResponse != null) {
                // Get saved state from API
                val addonSavedStateData = getAddOnSavedStateById(addOn?.basicInfo?.id
                        ?: "", addOnSavedStateResponse)
                initialSelectedState = addonSavedStateData != null
                isAddOnSelected = initialSelectedState
                initialAddOnNoteTo = addonSavedStateData?.addOnMetadata?.addOnNote?.to ?: ""
                addOnNoteTo = initialAddOnNoteTo
                initialAddOnNoteFrom = addonSavedStateData?.addOnMetadata?.addOnNote?.from ?: ""
                addOnNoteFrom = initialAddOnNoteFrom
                initialAddOnNote = addonSavedStateData?.addOnMetadata?.addOnNote?.notes ?: ""
                addOnNote = initialAddOnNote
            } else {
                // Get saved state from previous page (Checkout / OSP)
                val addonSavedStateData = getAddOnSavedStateById(addOn?.basicInfo?.id
                        ?: "", addOnProductData.availableBottomSheetData.addOnSavedStates)
                initialSelectedState = addonSavedStateData != null
                isAddOnSelected = initialSelectedState
                initialAddOnNoteTo = addonSavedStateData?.addOnMetadata?.addOnNote?.to ?: ""
                addOnNoteTo = initialAddOnNoteTo
                initialAddOnNoteFrom = addonSavedStateData?.addOnMetadata?.addOnNote?.from ?: ""
                addOnNoteFrom = initialAddOnNoteFrom
                initialAddOnNote = addonSavedStateData?.addOnMetadata?.addOnNote?.notes ?: ""
                addOnNote = initialAddOnNote
            }
            isCustomNote = addOn?.basicInfo?.rules?.customNote ?: false
            onlyGreetingCardInfo = addOnProductData.availableBottomSheetData.addOnInfoWording.onlyGreetingCard
            packagingAndGreetingCardInfo = addOnProductData.availableBottomSheetData.addOnInfoWording.packagingAndGreetingCard
            invoiceNotSentToRecipientInfo = addOnProductData.availableBottomSheetData.addOnInfoWording.invoiceNotSendToRecipient
            isLoadingNoteState = false
        }
    }

    private fun getAddOnSavedStateById(addOnId: String, addOnSavedStateResponse: GetAddOnSavedStateResponse): AddOnDataResponse? {
        if (addOnId.isNotBlankOrZero()) {
            addOnSavedStateResponse.getAddOns.data.addOns.forEach {
                it.addOnData.forEach {
                    if (it.addOnId == addOnId) {
                        return it
                    }
                }
            }
        }

        return null
    }

    private fun getAddOnSavedStateById(addOnId: String, addOnSavedStateResults: List<AddOnData>): AddOnData? {
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