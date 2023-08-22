package com.tokopedia.checkout.view.converter

import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import javax.inject.Inject

class RatesDataConverter @Inject constructor() {

    fun getShipmentDetailData(
        shipmentCartItemModel: ShipmentCartItemModel,
        recipientAddressModel: RecipientAddressModel
    ): ShipmentDetailData {
        val shipmentDetailData = ShipmentDetailData()
        val shipmentCartData = shipmentCartItemModel.shipmentCartData
        shipmentCartData.destinationAddress = recipientAddressModel.street
        shipmentCartData.destinationDistrictId = recipientAddressModel.destinationDistrictId
        shipmentCartData.destinationLatitude = recipientAddressModel.latitude
        shipmentCartData.destinationLongitude = recipientAddressModel.longitude
        shipmentCartData.destinationPostalCode = recipientAddressModel.postalCode
        shipmentDetailData.shipmentCartData = shipmentCartData
        var totalQuantity = 0
        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
            if (!cartItemModel.isError) {
                totalQuantity += cartItemModel.quantity
            }
        }
        shipmentDetailData.totalQuantity = totalQuantity
        shipmentDetailData.shopId = shipmentCartItemModel.shopId.toString()
        shipmentDetailData.isBlackbox = shipmentCartItemModel.isBlackbox
        if (recipientAddressModel.selectedTabIndex == 1 && recipientAddressModel.locationDataModel != null) {
            shipmentDetailData.addressId = recipientAddressModel.locationDataModel.addrId
        } else {
            shipmentDetailData.addressId = shipmentCartItemModel.addressId
        }
        shipmentDetailData.preorder = shipmentCartItemModel.isProductIsPreorder
        return shipmentDetailData
    }

    fun getShipmentCartData(
        userAddress: UserAddress,
        groupShop: GroupShop,
        shipmentCartItemModel: ShipmentCartItemModel,
        keroToken: String,
        keroUnixTime: String
    ): ShipmentCartData {
        var orderValue: Long = 0
        var totalWeight = 0
        var totalWeightActual = 0
        var preOrderDuration = 0
        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
            if (!cartItemModel.isError) {
                orderValue += (cartItemModel.quantity * cartItemModel.price).toLong()
                totalWeight += (cartItemModel.quantity * cartItemModel.weight).toInt()
                val weightActual: Double = if (cartItemModel.weightActual > 0) {
                    cartItemModel.weightActual
                } else {
                    cartItemModel.weight
                }
                totalWeightActual += (cartItemModel.quantity * weightActual).toInt()
                preOrderDuration = cartItemModel.preOrderDurationDay
            }
        }
        return ShipmentCartData(
            token = keroToken,
            ut = keroUnixTime,
            destinationAddress = userAddress.address,
            destinationDistrictId = userAddress.districtId,
            destinationLatitude =
            userAddress.latitude.ifEmpty { null },
            destinationLongitude =
            userAddress.longitude.ifEmpty { null },
            destinationPostalCode = userAddress.postalCode,
            originDistrictId = groupShop.groupShopData.first().shop.districtId,
            originLatitude =
            groupShop.groupShopData.first().shop.latitude.ifEmpty { null },
            originLongitude =
            groupShop.groupShopData.first().shop.longitude.ifEmpty { null },
            originPostalCode = groupShop.groupShopData.first().shop.postalCode,
            categoryIds = getCategoryIds(groupShop.groupShopData.flatMap { it.products }),
            productInsurance = if (isForceInsurance(groupShop.groupShopData.flatMap { it.products })) 1 else 0,
            shopShipments = groupShop.shopShipments,
            insurance = 1,
            boMetadata = groupShop.boMetadata,
            orderValue = orderValue,
            weight = totalWeight.toDouble(),
            weightActual = totalWeightActual.toDouble(),
            preOrderDuration = preOrderDuration,
            isFulfillment = shipmentCartItemModel.isFulfillment,
            shopTier = shipmentCartItemModel.shopTypeInfoData.shopTier,
            groupType = shipmentCartItemModel.groupType
        )
    }

    private fun getCategoryIds(products: List<Product>): String {
        val categoryIds: MutableList<Int> = ArrayList()
        for (i in products.indices) {
            if (!products[i].isError) {
                val categoryId = products[i].productCatId
                if (!categoryIds.contains(categoryId)) {
                    categoryIds.add(categoryId)
                }
            }
        }
        return categoryIds.joinToString(",")
    }

    private fun isForceInsurance(products: List<Product>): Boolean {
        for (product in products) {
            if (!product.isError && product.isProductFinsurance) {
                return true
            }
        }
        return false
    }

    companion object {
        @JvmStatic
        fun getLogisticPromoCode(itemModel: ShipmentCartItemModel): String {
            return itemModel.voucherLogisticItemUiModel?.code ?: ""
        }

        fun getLogisticPromoCode(orderModel: CheckoutOrderModel): String {
            return orderModel.shipment.courierItemData?.selectedShipper?.logPromoCode ?: ""
        }
    }
}
