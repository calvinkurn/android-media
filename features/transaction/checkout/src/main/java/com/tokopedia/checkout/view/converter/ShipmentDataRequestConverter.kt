package com.tokopedia.checkout.view.converter

import com.google.gson.Gson
import com.tokopedia.checkout.data.model.request.checkout.Bundle
import com.tokopedia.checkout.data.model.request.checkout.BundleInfo
import com.tokopedia.checkout.data.model.request.checkout.CheckoutGiftingAddOn
import com.tokopedia.checkout.data.model.request.checkout.Data
import com.tokopedia.checkout.data.model.request.checkout.Dropship
import com.tokopedia.checkout.data.model.request.checkout.GroupOrder
import com.tokopedia.checkout.data.model.request.checkout.OrderFeature
import com.tokopedia.checkout.data.model.request.checkout.OrderMetadata
import com.tokopedia.checkout.data.model.request.checkout.Product
import com.tokopedia.checkout.data.model.request.checkout.Promo
import com.tokopedia.checkout.data.model.request.checkout.ShippingInfo
import com.tokopedia.checkout.data.model.request.checkout.ShopOrder
import com.tokopedia.checkout.data.model.request.common.OntimeDeliveryGuarantee
import com.tokopedia.checkout.data.model.request.common.RatesFeature
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.SelectedShipperModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ONS_PRODUCT_SERVICE
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import javax.inject.Inject

/**
 * @author Irfan Khoirul on 07/03/18
 * Originally Authored by Kris, Aghny
 */
class ShipmentDataRequestConverter @Inject constructor(private val _gson: Gson) {

    fun createCheckoutRequestData(
        shipmentCartItemModels: List<CheckoutOrderModel>,
        recipientAddress: RecipientAddressModel,
        isTradeInPickup: Boolean
    ): List<Data> {
        val addressId = getSelectedAddressId(recipientAddress)
        val data = Data()
        data.addressId = addressId.toLongOrZero()
        data.groupOrders = shipmentCartItemModels.mapNotNull { shipmentCartItemModel ->
            var groupOrder: GroupOrder? = null
            val shipmentDetailData = shipmentCartItemModel.shipment
//            if (shipmentDetailData != null && (
//                shipmentDetailData.selectedCourier != null ||
//                    shipmentDetailData.selectedCourierTradeInDropOff != null
//                )
//            ) {
            var courierItemData: CourierItemData? = null
                /*if (isTradeInPickup && shipmentDetailData.selectedCourierTradeInDropOff != null) {
                    courierItemData = shipmentDetailData.selectedCourierTradeInDropOff
                } else*/ if (!isTradeInPickup && shipmentDetailData.courierItemData != null) {
                courierItemData = shipmentDetailData.courierItemData
            }
            if (courierItemData != null) {
                val ratesFeature = generateRatesFeatureNew(courierItemData)
                val selectedShipper = courierItemData.selectedShipper

                // Create shop product model for shipment
                val shippingInfoCheckoutRequest = ShippingInfo()
                shippingInfoCheckoutRequest.shippingId = selectedShipper.shipperId.toLong()
                shippingInfoCheckoutRequest.spId = selectedShipper.shipperProductId.toLong()
                val scheduleDeliveryUiModel = courierItemData.scheduleDeliveryUiModel
                if (scheduleDeliveryUiModel?.isSelected == true) {
                    shippingInfoCheckoutRequest.ratesId =
                        if (scheduleDeliveryUiModel.ratesId != 0L) {
                            scheduleDeliveryUiModel.ratesId.toString()
                        } else {
                            ""
                        }
                } else {
                    shippingInfoCheckoutRequest.ratesId =
                        shipmentDetailData.shippingCourierUiModels.firstOrNull()?.ratesId ?: ""
                }
                shippingInfoCheckoutRequest.checksum = selectedShipper.checksum ?: ""
                shippingInfoCheckoutRequest.ut = selectedShipper.ut ?: ""
                shippingInfoCheckoutRequest.ratesFeature = ratesFeature
                shippingInfoCheckoutRequest.finsurance = if (shipmentDetailData.insurance.isCheckInsurance) 1 else 0
                val promoCodes = ArrayList<String>()
                val promoRequests: ArrayList<Promo> = ArrayList()
                val voucherLogisticItemUiModel = shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode
                if (voucherLogisticItemUiModel != null) {
                    promoCodes.add(voucherLogisticItemUiModel)
                    val promoRequest = Promo()
                    promoRequest.code = voucherLogisticItemUiModel
                    promoRequest.type = Promo.TYPE_LOGISTIC
                    promoRequests.add(promoRequest)
                }
                val shopOrders = shipmentCartItemModel.cartItemModelsGroupByOrder.map {
                    ShopOrder(
                        bundle = mapBundleNew(it.value),
                        cartStringOrder = it.key,
                        isPreorder = if (shipmentCartItemModel.isProductIsPreorder) 1 else 0,
                        orderFeature = OrderFeature(
                            isOrderPriority = 0
                        ),
                        promos = promoRequests,
                        shopId = shipmentCartItemModel.shopId,
                        warehouseId = shipmentCartItemModel.fulfillmentId,
                        isTokoNow = shipmentCartItemModel.isTokoNow
                    )
                }
                groupOrder = GroupOrder(
                    groupType = shipmentCartItemModel.groupType,
                    cartStringGroup = shipmentCartItemModel.cartStringGroup,
                    shippingInfo = shippingInfoCheckoutRequest,
                    dropship = Dropship(
                        isDropship = 0,
                        name = "",
                        telpNo = ""
                    ),
                    checkoutGiftingOrderLevel = mapAddOnsProduct(shipmentCartItemModel.addOnsOrderLevelModel, AddOnProductDataModel()),
                    orderMetadata = mapOrderMetadata(shipmentCartItemModel, selectedShipper, promoRequests),
                    shopOrders = shopOrders
                )
            }
//            }
            groupOrder
        }
        return listOf(data)
    }
    fun createCheckoutRequestDataNew(
        shipmentCartItemModels: List<ShipmentCartItemModel>,
        recipientAddress: RecipientAddressModel,
        isTradeInPickup: Boolean
    ): List<Data> {
        val addressId = getSelectedAddressId(recipientAddress)
        val data = Data()
        data.addressId = addressId.toLongOrZero()
        data.groupOrders = shipmentCartItemModels.mapNotNull { shipmentCartItemModel ->
            var groupOrder: GroupOrder? = null
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
                    val ratesFeature = generateRatesFeatureNew(courierItemData)
                    val selectedShipper = courierItemData.selectedShipper

                    // Create shop product model for shipment
                    val shippingInfoCheckoutRequest = ShippingInfo()
                    shippingInfoCheckoutRequest.shippingId = selectedShipper.shipperId.toLong()
                    shippingInfoCheckoutRequest.spId = selectedShipper.shipperProductId.toLong()
                    val scheduleDeliveryUiModel = courierItemData.scheduleDeliveryUiModel
                    if (scheduleDeliveryUiModel?.isSelected == true) {
                        shippingInfoCheckoutRequest.ratesId =
                            if (scheduleDeliveryUiModel.ratesId != 0L) {
                                scheduleDeliveryUiModel.ratesId.toString()
                            } else {
                                ""
                            }
                    } else {
                        shippingInfoCheckoutRequest.ratesId =
                            shipmentDetailData.shippingCourierViewModels?.first()?.ratesId ?: ""
                    }
                    shippingInfoCheckoutRequest.checksum = selectedShipper.checksum ?: ""
                    shippingInfoCheckoutRequest.ut = selectedShipper.ut ?: ""
                    shippingInfoCheckoutRequest.ratesFeature = ratesFeature
                    shippingInfoCheckoutRequest.finsurance = if (shipmentDetailData.useInsurance == true) 1 else 0
                    val promoCodes = ArrayList<String>()
                    val promoRequests: ArrayList<Promo> = ArrayList()
                    val voucherLogisticItemUiModel = shipmentCartItemModel.voucherLogisticItemUiModel
                    if (voucherLogisticItemUiModel != null) {
                        promoCodes.add(voucherLogisticItemUiModel.code)
                        val promoRequest = Promo()
                        promoRequest.code = voucherLogisticItemUiModel.code
                        promoRequest.type = Promo.TYPE_LOGISTIC
                        promoRequests.add(promoRequest)
                    }
                    val shopOrders = shipmentCartItemModel.cartItemModelsGroupByOrder.map {
                        ShopOrder(
                            bundle = mapBundle(it.value),
                            cartStringOrder = it.key,
                            isPreorder = if (shipmentCartItemModel.isProductIsPreorder) 1 else 0,
                            orderFeature = OrderFeature(
                                isOrderPriority = if (shipmentDetailData.isOrderPriority == true) 1 else 0
                            ),
                            promos = promoRequests,
                            shopId = it.value.first().shopId.toLongOrZero(),
                            warehouseId = shipmentCartItemModel.fulfillmentId,
                            isTokoNow = shipmentCartItemModel.isTokoNow
                        )
                    }
                    groupOrder = GroupOrder(
                        groupType = shipmentCartItemModel.groupType,
                        cartStringGroup = shipmentCartItemModel.cartStringGroup,
                        shippingInfo = shippingInfoCheckoutRequest,
                        dropship = Dropship(
                            isDropship = if (shipmentDetailData.useDropshipper == true) 1 else 0,
                            name = shipmentDetailData.dropshipperName ?: "",
                            telpNo = shipmentDetailData.dropshipperPhone ?: ""
                        ),
                        checkoutGiftingOrderLevel = mapAddOnsProduct(shipmentCartItemModel.addOnsOrderLevelModel, AddOnProductDataModel()),
                        orderMetadata = mapOrderMetadata(shipmentCartItemModel, selectedShipper, promoRequests),
                        shopOrders = shopOrders
                    )
                }
            }
            groupOrder
        }
        return listOf(data)
    }

    private fun mapBundleNew(cartItemModels: List<CheckoutProductModel>): List<Bundle> {
        val bundleList = mutableListOf<Bundle>()

        val bundleIdProductsMap = mutableMapOf<String, MutableList<Product>>()
        val bundleIdGroupIdMap = mutableMapOf<String, String>()
        cartItemModels.forEach {
            if (!bundleIdProductsMap.containsKey(it.bundleId)) {
                val product = mapProductNew(it)
                bundleIdProductsMap[it.bundleId] = mutableListOf(product)
                bundleIdGroupIdMap[it.bundleId] = it.bundleGroupId
            } else {
                val products = bundleIdProductsMap[it.bundleId]
                val product = mapProductNew(it)
                products?.add(product)
            }
        }

        bundleIdProductsMap.forEach {
            val bundle = Bundle().apply {
                bundleInfo = BundleInfo().apply {
                    if (it.key.isNotBlankOrZero()) {
                        bundleId = it.key.toLongOrZero()
                        bundleGroupId = bundleIdGroupIdMap[it.key] ?: ""
                    }
                }
                productData = it.value
            }
            bundleList.add(bundle)
        }

        return bundleList
    }

    private fun mapProductNew(it: CheckoutProductModel): Product {
        val product = Product().apply {
            productId = it.productId.toString()
            isPpp = it.isProtectionOptIn
            checkoutGiftingProductLevel = mapAddOnsProduct(it.addOnGiftingProductLevelModel, it.addOnProduct)
            cartId = it.cartId.toString()
            productCategoryId = it.productCatId.toString()
            protectionPricePerProduct = it.protectionPricePerProduct
            protectionTitle = it.protectionTitle
            isProtectionAvailable = it.isProtectionAvailable
        }
        return product
    }

    private fun mapBundle(cartItemModels: List<CartItemModel>): List<Bundle> {
        val bundleList = mutableListOf<Bundle>()

        val bundleIdProductsMap = mutableMapOf<String, MutableList<Product>>()
        val bundleIdGroupIdMap = mutableMapOf<String, String>()
        cartItemModels.forEach {
            if (!bundleIdProductsMap.containsKey(it.bundleId)) {
                val product = mapProduct(it)
                bundleIdProductsMap[it.bundleId] = mutableListOf(product)
                bundleIdGroupIdMap[it.bundleId] = it.bundleGroupId
            } else {
                val products = bundleIdProductsMap[it.bundleId]
                val product = mapProduct(it)
                products?.add(product)
            }
        }

        bundleIdProductsMap.forEach {
            val bundle = Bundle().apply {
                bundleInfo = BundleInfo().apply {
                    if (it.key.isNotBlankOrZero()) {
                        bundleId = it.key.toLongOrZero()
                        bundleGroupId = bundleIdGroupIdMap[it.key] ?: ""
                    }
                }
                productData = it.value
            }
            bundleList.add(bundle)
        }

        return bundleList
    }

    private fun mapProduct(it: CartItemModel): Product {
        val product = Product().apply {
            productId = it.productId.toString()
            isPpp = getRealIsPpp(it)
            checkoutGiftingProductLevel = mapAddOnsProduct(it.addOnGiftingProductLevelModel, it.addOnProduct)
            cartId = it.cartId.toString()
            productCategoryId = it.analyticsProductCheckoutData.productCategoryId
            protectionPricePerProduct = it.protectionPricePerProduct
            protectionTitle = it.protectionTitle
            isProtectionAvailable = it.isProtectionAvailable
        }
        return product
    }

    private fun getRealIsPpp(it: CartItemModel): Boolean {
        return if (it.addOnProduct.listAddOnProductData.firstOrNull { addon -> addon.type == AddOnConstant.PRODUCT_PROTECTION_INSURANCE_TYPE } == null) {
            it.isProtectionOptIn
        } else {
            false
        }
    }

    private fun mapAddOnsProduct(addOnsData: AddOnGiftingDataModel, addOnProduct: AddOnProductDataModel): List<CheckoutGiftingAddOn> {
        val listCheckoutGiftingAddOn = arrayListOf<CheckoutGiftingAddOn>()
        if (addOnsData.status == 1) {
            for (addOnItem in addOnsData.addOnsDataItemModelList) {
                val addOnGiftingRequest = CheckoutGiftingAddOn()
                addOnGiftingRequest.itemId = addOnItem.addOnId
                addOnGiftingRequest.itemUniqueId = addOnItem.addOnUniqueId
                addOnGiftingRequest.itemType = "add_ons"
                addOnGiftingRequest.itemQty = addOnItem.addOnQty.toInt()
                addOnGiftingRequest.itemMetadata = _gson.toJson(addOnItem.addOnMetadata)
                addOnGiftingRequest.itemUniqueId = addOnItem.addOnUniqueId
                listCheckoutGiftingAddOn.add(addOnGiftingRequest)
            }
        }
        addOnProduct.listAddOnProductData.forEach { addOnProductItem ->
            if (addOnProductItem.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK || addOnProductItem.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY) {
                val addOnGiftingRequest = CheckoutGiftingAddOn()
                addOnGiftingRequest.itemId = addOnProductItem.id.toString()
                addOnGiftingRequest.itemUniqueId = addOnProductItem.uniqueId
                addOnGiftingRequest.itemType = ADD_ONS_PRODUCT_SERVICE
                addOnGiftingRequest.itemQty = addOnProductItem.qty
                addOnGiftingRequest.itemMetadata = ""
                listCheckoutGiftingAddOn.add(addOnGiftingRequest)
            }
        }
        return listCheckoutGiftingAddOn
    }

    private fun mapOrderMetadata(
        shipmentCartItemModel: CheckoutOrderModel,
        selectedShipper: SelectedShipperModel,
        promos: List<Promo>
    ): List<OrderMetadata> {
        val orderMetadata = arrayListOf<OrderMetadata>()
        if (selectedShipper.freeShippingMetadata.isNotBlank() &&
            promos.firstOrNull { it.type == Promo.TYPE_LOGISTIC } != null
        ) {
            // only add free shipping metadata if the order contains at least 1 promo logistic
            orderMetadata.add(OrderMetadata(OrderMetadata.FREE_SHIPPING_METADATA, selectedShipper.freeShippingMetadata))
        }
        if (shipmentCartItemModel.hasEthicalProducts) {
            for (cartItemModel in shipmentCartItemModel.products) {
                if (!cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                    if (shipmentCartItemModel.prescriptionIds.isNotEmpty()) {
                        orderMetadata.add(
                            OrderMetadata(
                                OrderMetadata.UPLOAD_PRESCRIPTION_META_DATA_KEY,
                                shipmentCartItemModel.prescriptionIds.toString()
                            )
                        )
                    } else if (shipmentCartItemModel.consultationDataString.isNotEmpty()) {
                        orderMetadata.add(
                            OrderMetadata(
                                OrderMetadata.MINI_CONSULTATION_META_DATA_KEY,
                                shipmentCartItemModel.consultationDataString
                            )
                        )
                    }
                    break
                }
            }
        }
        if (selectedShipper.scheduleDate.isNotEmpty() && selectedShipper.timeslotId != 0L && shipmentCartItemModel.validationMetadata.isNotEmpty()) {
            orderMetadata.add(OrderMetadata(OrderMetadata.SCHEDULE_DELIVERY_META_DATA_KEY, shipmentCartItemModel.validationMetadata))
        }
        return orderMetadata
    }

    private fun mapOrderMetadata(
        shipmentCartItemModel: ShipmentCartItemModel,
        selectedShipper: SelectedShipperModel,
        promos: List<Promo>
    ): List<OrderMetadata> {
        val orderMetadata = arrayListOf<OrderMetadata>()
        if (selectedShipper.freeShippingMetadata.isNotBlank() &&
            promos.firstOrNull { it.type == Promo.TYPE_LOGISTIC } != null
        ) {
            // only add free shipping metadata if the order contains at least 1 promo logistic
            orderMetadata.add(OrderMetadata(OrderMetadata.FREE_SHIPPING_METADATA, selectedShipper.freeShippingMetadata))
        }
        if (shipmentCartItemModel.hasEthicalProducts) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (!cartItemModel.isError && cartItemModel.ethicalDrugDataModel.needPrescription) {
                    if (shipmentCartItemModel.prescriptionIds.isNotEmpty()) {
                        orderMetadata.add(
                            OrderMetadata(
                                OrderMetadata.UPLOAD_PRESCRIPTION_META_DATA_KEY,
                                shipmentCartItemModel.prescriptionIds.toString()
                            )
                        )
                    } else if (shipmentCartItemModel.consultationDataString.isNotEmpty()) {
                        orderMetadata.add(
                            OrderMetadata(
                                OrderMetadata.MINI_CONSULTATION_META_DATA_KEY,
                                shipmentCartItemModel.consultationDataString
                            )
                        )
                    }
                    break
                }
            }
        }
        if (selectedShipper.scheduleDate.isNotEmpty() && selectedShipper.timeslotId != 0L && shipmentCartItemModel.validationMetadata.isNotEmpty()) {
            orderMetadata.add(OrderMetadata(OrderMetadata.SCHEDULE_DELIVERY_META_DATA_KEY, shipmentCartItemModel.validationMetadata))
        }
        return orderMetadata
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

    private fun generateRatesFeatureNew(courierItemData: CourierItemData): com.tokopedia.checkout.data.model.request.checkout.RatesFeature {
        val result = com.tokopedia.checkout.data.model.request.checkout.RatesFeature()
        val otdg = com.tokopedia.checkout.data.model.request.checkout.OntimeDeliveryGuarantee()
        val ontimeDelivery = courierItemData.selectedShipper.ontimeDelivery
        if (ontimeDelivery != null) {
            otdg.available = ontimeDelivery.available
            otdg.duration = ontimeDelivery.value
        }
        result.ontimeDeliveryGuarantee = otdg
        return result
    }

    companion object {
        @JvmStatic
        fun generateRatesFeature(courierItemData: CourierItemData): RatesFeature {
            val result = RatesFeature()
            val otdg = OntimeDeliveryGuarantee()
            val ontimeDelivery = courierItemData.selectedShipper.ontimeDelivery
            if (ontimeDelivery != null) {
                otdg.available = ontimeDelivery.available
                otdg.duration = ontimeDelivery.value
            }
            result.ontimeDeliveryGuarantee = otdg
            return result
        }
    }
}
