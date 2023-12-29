package com.tokopedia.addongifting.addonbottomsheet.domain.mapper

import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.AddOnByProductRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.RequestData
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.SourceRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate.GetAddOnSavedStateRequest
import com.tokopedia.addongifting.addonbottomsheet.view.uimodel.AddOnUiModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnDataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnMetadataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnNoteRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.CartProduct
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData

object AddOnRequestMapper {

    fun generateGetAddOnByProductRequestParams(addOnProductData: AddOnProductData): GetAddOnByProductRequest {
        return GetAddOnByProductRequest().apply {
            addOnRequest = addOnProductData.availableBottomSheetData.products.map {
                var parentId = ""
                parentId = if (it.productParentId.isNotEmpty() && it.productParentId.toLongOrZero() > 0) {
                    it.productParentId
                } else {
                    it.productId
                }
                AddOnByProductRequest().apply {
                    productId = parentId
                    warehouseId = addOnProductData.availableBottomSheetData.warehouseId
                    addOnLevel = if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                        AddOnByProductRequest.ADD_ON_LEVEL_ORDER
                    } else {
                        AddOnByProductRequest.ADD_ON_LEVEL_PRODUCT
                    }
                }
            }
            sourceRequest = SourceRequest().apply {
                squad = SourceRequest.SQUAD_VALUE
                useCase = SourceRequest.USE_CASE_VALUE
            }
            requestData = RequestData().apply {
                inventory = true
                staticInfo = true
            }
        }
    }

    fun generateGetAddOnSavedStateRequestParams(addOnProductData: AddOnProductData): GetAddOnSavedStateRequest {
        return GetAddOnSavedStateRequest().apply {
            source = addOnProductData.source
            addOnKeys = listOf(
                if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                    "${addOnProductData.availableBottomSheetData.cartString}-0"
                } else {
                    "${addOnProductData.availableBottomSheetData.cartString}-${addOnProductData.availableBottomSheetData.products.firstOrNull()?.cartId ?: ""}"
                }
            )
        }
    }

    fun generateSaveAddOnStateRequestParams(addOnProductData: AddOnProductData, addOnUiModel: AddOnUiModel): SaveAddOnStateRequest {
        return SaveAddOnStateRequest().apply {
            source = addOnProductData.source
            addOns = listOf(
                AddOnRequest().apply {
                    if (addOnProductData.availableBottomSheetData.isTokoCabang) {
                        addOnKey = "${addOnProductData.availableBottomSheetData.cartString}-0"
                        addOnLevel = AddOnConstant.ADD_ON_LEVEL_ORDER
                    } else {
                        addOnKey = "${addOnProductData.availableBottomSheetData.cartString}-${addOnProductData.availableBottomSheetData.products.firstOrNull()?.cartId}"
                        addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT
                    }
                    cartProducts = addOnProductData.availableBottomSheetData.products.map {
                        CartProduct().apply {
                            cartId = it.cartId.toLongOrZero()
                            productId = it.productId.toLongOrZero()
                            warehouseId = addOnProductData.availableBottomSheetData.warehouseId.toLongOrZero()
                            productName = it.productName
                            productImageUrl = it.productImageUrl
                            productParentId = it.productParentId
                        }
                    }
                    if (addOnUiModel.isAddOnSelected) {
                        addOnData = listOf(
                            AddOnDataRequest().apply {
                                addOnId = addOnUiModel.addOnId.toLongOrZero()
                                addOnQty = addOnUiModel.addOnQty
                                addOnUniqueId = addOnUiModel.addOnUniqueId
                                addOnMetadata = AddOnMetadataRequest().apply {
                                    addOnNote = AddOnNoteRequest().apply {
                                        isCustomNote = addOnUiModel.isCustomNote
                                        to = addOnUiModel.addOnNoteTo
                                        from = addOnUiModel.addOnNoteFrom
                                        notes = addOnUiModel.addOnNote
                                    }
                                }
                            }
                        )
                    } else {
                        addOnData = emptyList()
                    }
                }
            )
        }
    }
}
