package com.tokopedia.checkout.view.converter

import com.google.gson.Gson
import com.tokopedia.checkout.data.model.request.checkout.old.AddOnGiftingRequest
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.DropshipDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ProductDataCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.PromoRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ShippingInfoCheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.old.ShopProductCheckoutRequest
import com.tokopedia.checkout.data.model.request.common.OntimeDeliveryGuarantee
import com.tokopedia.checkout.data.model.request.common.RatesFeature
import com.tokopedia.checkout.view.adapter.ShipmentAdapter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import javax.inject.Inject

/**
 * @author Irfan Khoirul on 07/03/18
 * Originally Authored by Kris, Aghny
 */
class ShipmentDataRequestConverter @Inject constructor(private val _gson: Gson) {

    fun generateRequestData(
        shipmentCartItemModels: List<ShipmentCartItemModel>?,
        recipientAddress: RecipientAddressModel?,
        isAnalyticsPurpose: Boolean,
        isTradeInPickup: Boolean
    ): ShipmentAdapter.RequestData {
        val requestData = ShipmentAdapter.RequestData()
        if (shipmentCartItemModels != null && shipmentCartItemModels.isNotEmpty()) {
            val shopProductCheckoutRequestList: MutableList<ShopProductCheckoutRequest> =
                ArrayList()
            if (recipientAddress != null) {
                for (shipmentCartItemModel in shipmentCartItemModels) {
                    if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                        val element = getProductCheckoutRequest(
                            shipmentCartItemModel,
                            isTradeInPickup
                        )
                        if (element != null) {
                            shopProductCheckoutRequestList.add(
                                element
                            )
                        }
                    } else if (isAnalyticsPurpose) {
                        shopProductCheckoutRequestList.add(
                            getProductCheckoutRequestForAnalytics(
                                shipmentCartItemModel
                            )
                        )
                    }
                }
                requestData.checkoutRequestData =
                    createCheckoutRequestData(shopProductCheckoutRequestList, recipientAddress)
            }
        }
        return requestData
    }

    private fun getProductCheckoutRequestForAnalytics(shipmentCartItemModel: ShipmentCartItemModel): ShopProductCheckoutRequest {
        // Create shop product model for shipment
        val shippingInfoCheckoutRequest = ShippingInfoCheckoutRequest()
        shippingInfoCheckoutRequest.shippingId = 0
        shippingInfoCheckoutRequest.spId = 0
        shippingInfoCheckoutRequest.ratesId = ""
        shippingInfoCheckoutRequest.checksum = ""
        shippingInfoCheckoutRequest.ut = ""
        shippingInfoCheckoutRequest.analyticsDataShippingCourierPrice = ""
        val shopProductCheckout = ShopProductCheckoutRequest()
        shopProductCheckout.shippingInfo = shippingInfoCheckoutRequest
        shopProductCheckout.fcancelPartial = 0
        shopProductCheckout.finsurance = 0
        shopProductCheckout.isOrderPriority = 0
        shopProductCheckout.isPreorder = if (shipmentCartItemModel.isProductIsPreorder) 1 else 0
        shopProductCheckout.shopId = shipmentCartItemModel.shopId
        shopProductCheckout.warehouseId = shipmentCartItemModel.fulfillmentId
        shopProductCheckout.cartString = shipmentCartItemModel.cartString
        shopProductCheckout.productData = convertToProductDataCheckout(shipmentCartItemModel)
        shopProductCheckout.isTokoNow = shipmentCartItemModel.isTokoNow
        shopProductCheckout.needToSendValidationMetadata = false
        shopProductCheckout.validationMetadata = ""
        if (shipmentCartItemModel.addOnsOrderLevelModel != null) {
            shopProductCheckout.giftingAddOnOrderLevel =
                convertGiftingAddOnModelRequest(shipmentCartItemModel.addOnsOrderLevelModel)
        }
        return shopProductCheckout
    }

    private fun getProductCheckoutRequest(
        shipmentCartItemModel: ShipmentCartItemModel,
        isTradeInPickup: Boolean
    ): ShopProductCheckoutRequest? {
        val shipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
        if (shipmentDetailData != null && (
            shipmentDetailData.selectedCourier != null ||
                shipmentDetailData.selectedCourierTradeInDropOff != null
            )
        ) {
            var courierItemData: CourierItemData? = null
            if (isTradeInPickup && shipmentDetailData.selectedCourierTradeInDropOff != null) {
                courierItemData = shipmentDetailData.selectedCourierTradeInDropOff
            } else if (!isTradeInPickup && shipmentDetailData.selectedCourier != null) {
                courierItemData = shipmentDetailData.selectedCourier
            }
            if (courierItemData != null) {
                val ratesFeature = generateRatesFeature(courierItemData)
                val selectedShipper = courierItemData.selectedShipper

                // Create shop product model for shipment
                val shippingInfoCheckoutRequest = ShippingInfoCheckoutRequest()
                shippingInfoCheckoutRequest.shippingId = selectedShipper.shipperId
                shippingInfoCheckoutRequest.spId = selectedShipper.shipperProductId
                if (courierItemData.scheduleDeliveryUiModel != null && courierItemData.scheduleDeliveryUiModel!!.isSelected) {
                    shippingInfoCheckoutRequest.ratesId =
                        if (courierItemData.scheduleDeliveryUiModel!!.ratesId != 0L) courierItemData.scheduleDeliveryUiModel!!.ratesId.toString() else ""
                } else {
                    shippingInfoCheckoutRequest.ratesId =
                        if (shipmentDetailData.shippingCourierViewModels != null) shipmentDetailData.shippingCourierViewModels!![0].ratesId else ""
                }
                shippingInfoCheckoutRequest.checksum = selectedShipper.checksum
                shippingInfoCheckoutRequest.ut = selectedShipper.ut
                shippingInfoCheckoutRequest.analyticsDataShippingCourierPrice =
                    selectedShipper.shipperPrice.toString()
                shippingInfoCheckoutRequest.ratesFeature = ratesFeature
                val shopProductCheckout = ShopProductCheckoutRequest()
                shopProductCheckout.shippingInfo = shippingInfoCheckoutRequest
                shopProductCheckout.fcancelPartial =
                    if (shipmentDetailData.usePartialOrder) 1 else 0
                shopProductCheckout.finsurance =
                    if (shipmentDetailData.useInsurance != null && shipmentDetailData.useInsurance!!) 1 else 0
                shopProductCheckout.isOrderPriority =
                    if (shipmentDetailData.isOrderPriority != null && shipmentDetailData.isOrderPriority!!) 1 else 0
                shopProductCheckout.isPreorder =
                    if (shipmentCartItemModel.isProductIsPreorder) 1 else 0
                shopProductCheckout.shopId = shipmentCartItemModel.shopId
                shopProductCheckout.warehouseId = shipmentCartItemModel.fulfillmentId
                shopProductCheckout.cartString = shipmentCartItemModel.cartString
                shopProductCheckout.productData =
                    convertToProductDataCheckout(shipmentCartItemModel)
                shopProductCheckout.isTokoNow = shipmentCartItemModel.isTokoNow
                shopProductCheckout.needToSendValidationMetadata =
                    (selectedShipper.scheduleDate != "" && selectedShipper.timeslotId != 0L && shipmentCartItemModel.validationMetadata != "")
                shopProductCheckout.validationMetadata = shipmentCartItemModel.validationMetadata
                val promoCodes = ArrayList<String>()
                val promoRequests: MutableList<PromoRequest> = ArrayList()
                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                    promoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                    val promoRequest = PromoRequest()
                    promoRequest.code = shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                    promoRequest.type = PromoRequest.TYPE_LOGISTIC
                    promoRequests.add(promoRequest)
                    shopProductCheckout.freeShippingMetadata = selectedShipper.freeShippingMetadata
                }
                shopProductCheckout.promos = promoRequests
                if (promoCodes.size > 0) {
                    shopProductCheckout.promoCodes = promoCodes
                }
                if (shipmentDetailData.useDropshipper != null && shipmentDetailData.useDropshipper!!) {
                    val dropshipDataCheckoutRequest = DropshipDataCheckoutRequest()
                    dropshipDataCheckoutRequest.name = shipmentDetailData.dropshipperName
                    dropshipDataCheckoutRequest.telpNo = shipmentDetailData.dropshipperPhone
                    shopProductCheckout.isDropship = 1
                    shopProductCheckout.dropshipData = dropshipDataCheckoutRequest
                } else {
                    shopProductCheckout.isDropship = 0
                }
                if (shipmentCartItemModel.addOnsOrderLevelModel != null) {
                    shopProductCheckout.giftingAddOnOrderLevel =
                        convertGiftingAddOnModelRequest(shipmentCartItemModel.addOnsOrderLevelModel)
                }
                if (shipmentCartItemModel.hasEthicalProducts) {
                    for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                        if (!cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                            shopProductCheckout.needPrescription = true
                            shopProductCheckout.prescriptionIds =
                                shipmentCartItemModel.prescriptionIds
                            shopProductCheckout.consultationDataString =
                                shipmentCartItemModel.consultationDataString
                            break
                        }
                    }
                }
                return shopProductCheckout
            }
            return null
        }
        return null
    }

    private fun convertToProductDataCheckout(shipmentCartItemModel: ShipmentCartItemModel): ArrayList<ProductDataCheckoutRequest> {
        val productDataList = ArrayList<ProductDataCheckoutRequest>()
        for (cartItem in shipmentCartItemModel.cartItemModels) {
            productDataList.add(
                convertToProductDataCheckout(
                    cartItem,
                    shipmentCartItemModel.selectedShipmentDetailData
                )
            )
        }
        return productDataList
    }

    private fun convertToProductDataCheckout(
        cartItem: CartItemModel,
        shipmentDetailData: ShipmentDetailData?
    ): ProductDataCheckoutRequest {
        var courierId = ""
        var serviceId = ""
        var shippingPrice = ""
        if (shipmentDetailData?.selectedCourier != null) {
            courierId =
                shipmentDetailData.selectedCourier!!.selectedShipper.shipperProductId.toString()
            serviceId = shipmentDetailData.selectedCourier!!.selectedShipper.serviceId.toString()
            shippingPrice =
                shipmentDetailData.selectedCourier!!.selectedShipper.shipperPrice.toString()
        }
        val productDataCheckoutRequest = ProductDataCheckoutRequest()
        productDataCheckoutRequest.productId = cartItem.productId
        productDataCheckoutRequest.bundleId = cartItem.bundleId
        productDataCheckoutRequest.bundleGroupId = cartItem.bundleGroupId
        productDataCheckoutRequest.bundleType = cartItem.bundleType
        productDataCheckoutRequest.isPurchaseProtection = cartItem.isProtectionOptIn
        productDataCheckoutRequest.productName = cartItem.analyticsProductCheckoutData.productName
        productDataCheckoutRequest.productPrice = cartItem.analyticsProductCheckoutData.productPrice
        productDataCheckoutRequest.productBrand = cartItem.analyticsProductCheckoutData.productBrand
        productDataCheckoutRequest.productCategory =
            cartItem.analyticsProductCheckoutData.productCategory
        productDataCheckoutRequest.productVariant =
            cartItem.analyticsProductCheckoutData.productVariant
        productDataCheckoutRequest.productQuantity =
            cartItem.analyticsProductCheckoutData.productQuantity
        productDataCheckoutRequest.productShopId =
            cartItem.analyticsProductCheckoutData.productShopId
        productDataCheckoutRequest.productShopType =
            cartItem.analyticsProductCheckoutData.productShopType
        productDataCheckoutRequest.productShopName =
            cartItem.analyticsProductCheckoutData.productShopName
        productDataCheckoutRequest.productCategoryId =
            cartItem.analyticsProductCheckoutData.productCategoryId
        productDataCheckoutRequest.productListName =
            cartItem.analyticsProductCheckoutData.productListName
        productDataCheckoutRequest.productAttribution =
            cartItem.analyticsProductCheckoutData.productAttribution
        productDataCheckoutRequest.cartId = cartItem.cartId
        productDataCheckoutRequest.warehouseId = cartItem.analyticsProductCheckoutData.warehouseId
        productDataCheckoutRequest.productWeight =
            cartItem.analyticsProductCheckoutData.productWeight
        productDataCheckoutRequest.promoCode = cartItem.analyticsProductCheckoutData.promoCode
        productDataCheckoutRequest.promoDetails = cartItem.analyticsProductCheckoutData.promoDetails
        productDataCheckoutRequest.buyerAddressId =
            cartItem.analyticsProductCheckoutData.buyerAddressId
        productDataCheckoutRequest.shippingDuration = serviceId
        productDataCheckoutRequest.courier = courierId
        productDataCheckoutRequest.shippingPrice = shippingPrice
        productDataCheckoutRequest.codFlag = cartItem.analyticsProductCheckoutData.codFlag
        productDataCheckoutRequest.tokopediaCornerFlag =
            cartItem.analyticsProductCheckoutData.tokopediaCornerFlag
        productDataCheckoutRequest.isFulfillment =
            cartItem.analyticsProductCheckoutData.isFulfillment
        productDataCheckoutRequest.isDiscountedPrice =
            cartItem.analyticsProductCheckoutData.isDiscountedPrice
        productDataCheckoutRequest.isFreeShipping = cartItem.isFreeShipping
        productDataCheckoutRequest.isFreeShippingExtra = cartItem.isFreeShippingExtra
        productDataCheckoutRequest.freeShippingName = cartItem.freeShippingName
        productDataCheckoutRequest.campaignId = cartItem.analyticsProductCheckoutData.campaignId
        productDataCheckoutRequest.protectionPricePerProduct = cartItem.protectionPricePerProduct
        productDataCheckoutRequest.protectionTitle = cartItem.protectionTitle
        productDataCheckoutRequest.isProtectionAvailable = cartItem.isProtectionAvailable
        productDataCheckoutRequest.addOnGiftingProductLevelRequest =
            convertGiftingAddOnModelRequest(cartItem.addOnProductLevelModel)
        return productDataCheckoutRequest
    }

    private fun convertGiftingAddOnModelRequest(addOnsDataModel: AddOnsDataModel?): ArrayList<AddOnGiftingRequest> {
        val listAddOnProductRequest = ArrayList<AddOnGiftingRequest>()
        if (addOnsDataModel!!.status == 1) {
            for ((_, addOnId, addOnMetadata, addOnQty) in addOnsDataModel.addOnsDataItemModelList) {
                val addOnGiftingRequest = AddOnGiftingRequest()
                addOnGiftingRequest.itemId = addOnId
                addOnGiftingRequest.itemType = "add_ons"
                addOnGiftingRequest.itemQty = addOnQty.toInt()
                addOnGiftingRequest.itemMetadata = _gson.toJson(addOnMetadata)
                listAddOnProductRequest.add(addOnGiftingRequest)
            }
        }
        return listAddOnProductRequest
    }

    private fun createCheckoutRequestData(
        shopProducts: List<ShopProductCheckoutRequest>,
        recipientAddress: RecipientAddressModel
    ): List<DataCheckoutRequest> {
        val addressId = getSelectedAddressId(recipientAddress)
        val checkoutRequestData: MutableList<DataCheckoutRequest> = ArrayList()
        val dataCheckoutRequest = DataCheckoutRequest()
        dataCheckoutRequest.addressId = addressId
        dataCheckoutRequest.shopProducts = shopProducts
        checkoutRequestData.add(dataCheckoutRequest)
        return checkoutRequestData
    }

    private fun getSelectedAddressId(recipientAddress: RecipientAddressModel?): String {
        return if (recipientAddress != null) {
            if (recipientAddress.selectedTabIndex == 1 && recipientAddress.locationDataModel != null) {
                recipientAddress.locationDataModel.addrId
            } else {
                recipientAddress.id
            }
        } else {
            "0"
        }
    }

    companion object {
        @JvmStatic
        fun generateRatesFeature(courierItemData: CourierItemData): RatesFeature {
            val result = RatesFeature()
            val otdg = OntimeDeliveryGuarantee()
            if (courierItemData.selectedShipper.ontimeDelivery != null) {
                otdg.available = courierItemData.selectedShipper.ontimeDelivery!!.available
                otdg.duration = courierItemData.selectedShipper.ontimeDelivery!!.value
            }
            result.ontimeDeliveryGuarantee = otdg
            return result
        }
    }
}
