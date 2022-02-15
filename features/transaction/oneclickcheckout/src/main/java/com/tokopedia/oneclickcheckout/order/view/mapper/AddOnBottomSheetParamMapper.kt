package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.logisticcart.shipping.model.AddOnsDataModel
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.*

object AddOnBottomSheetParamMapper {

    fun generateParam(addOn: AddOnsDataModel,
                      addOnWordingData: AddOnWordingData,
                      product: OrderProduct,
                      shop: OrderShop): AddOnProductData {
        return AddOnProductData().apply {
            bottomSheetType = AddOnProductData.ADD_ON_BOTTOM_SHEET
            bottomSheetTitle = addOn.addOnsBottomSheetModel.headerTitle
            source = AddOnProductData.SOURCE_ONE_CLICK_CHECKOUT
            availableBottomSheetData = AvailableBottomSheetData().apply {
                products = listOf(Product().apply {
                    cartId = product.cartId
                    productId = product.productId.toString()
                    productName = product.productName
                    productImageUrl = product.productImageUrl
                    productPrice = product.productPrice
                })
                isTokoCabang = shop.isFulfillment
                cartString = shop.cartString
                warehouseId = shop.warehouseId.toString()
                shopName = shop.shopName
                addOnInfoWording = addOnWordingData
                addOnSavedStates = addOn.addOnsDataItemModelList.map {
                    AddOnData().apply {
                        addOnId = it.addOnId.toString()
                        addOnPrice = it.addOnPrice
                        addOnQty = it.addOnQty.toInt()
                        addOnMetadata = AddOnMetadata().apply {
                            addOnNote = AddOnNote().apply {
                                from = ""
                                to = ""
                                notes = ""
                                isCustomNote = true
                            }
                        }
                    }
                }
            }
        }

    }

}