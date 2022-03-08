package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class ShipmentCartItemModel(
        var isAllItemError: Boolean = false,
        var isError: Boolean = false,
        var errorTitle: String? = null,
        var errorDescription: String? = null,
        var isHasUnblockingError: Boolean = false,
        var unblockingErrorMessage: String? = null,
        var firstProductErrorIndex: Int = -1,
        var isTriggerScrollToErrorProduct: Boolean = false,

        var shipmentCartData: ShipmentCartData? = null,
        var selectedShipmentDetailData: ShipmentDetailData? = null,
        var shopShipmentList: List<ShopShipment>? = null,

        // Shop data
        var shopId: Long = 0,
        var shopName: String? = null,
        var orderNumber: Int = 0,
        var preOrderInfo: String? = null,
        var isFreeShippingExtra: Boolean = false,
        var freeShippingBadgeUrl: String? = null,
        var shopLocation: String? = null,
        var shopAlertMessage: String? = null,
        var shopTypeInfoData: ShopTypeInfoData? = null,
        var isTokoNow: Boolean = false,
        var shopTickerTitle: String = "",
        var shopTicker: String = "",

        // Cart item state
        var cartString: String? = null,
        var shippingId: Int = 0,
        var spId: Int = 0,
        var dropshiperName: String? = null,
        var dropshiperPhone: String? = null,
        var isInsurance: Boolean = false,
        var isSaveStateFlag: Boolean = false,

        var weightUnit: Int = 0,
        var isProductFinsurance: Boolean = false,
        var isProductFcancelPartial: Boolean = false,
        var isProductIsPreorder: Boolean = false,

        var cartItemModels: List<CartItemModel> = ArrayList(),

        // View state
        var isStateDetailSubtotalViewExpanded: Boolean = false,
        var isStateAllItemViewExpanded: Boolean = true,
        var isStateDropshipperDetailExpanded: Boolean = false,
        var isStateDropshipperHasError: Boolean = false,
        var isStateLoadingCourierState: Boolean = false,
        var isStateHasLoadCourierState: Boolean = false,
        var isStateHasLoadCourierTradeInDropOffState: Boolean = false,
        var isStateHasExtraMarginTop: Boolean = false,

        // Flag for courier recommendation
        private val useCourierRecommendation: Boolean = false,
        var isHidingCourier: Boolean = false,

        // for robinhood III
        var isBlackbox: Boolean = false,
        var addressId: String? = null,
        var blackboxInfo: String? = null,

        var isFulfillment: Boolean = false,
        var fulfillmentBadgeUrl: String? = null,
        var fulfillmentId: Long = 0,

        // promo stacking
        var hasPromoList: Boolean = false,
        var voucherLogisticItemUiModel: VoucherLogisticItemUiModel? = null,

        var isLeasingProduct: Boolean = false,
        var bookingFee: Int = 0,
        var listPromoCodes: List<String>? = null,

        var isDropshipperDisable: Boolean = false,
        var isOrderPrioritasDisable: Boolean = false,

        var isHasSetDropOffLocation: Boolean = false,

        // Shipping experiance
        var isEligibleNewShippingExperience: Boolean = false,
        var isTriggerShippingVibrationAnimation: Boolean = false,
        var isShippingBorderRed: Boolean = false,
        var isDisableChangeCourier: Boolean = false,
        var isAutoCourierSelection: Boolean = false,
        var hasGeolocation: Boolean = false,

        // regular shipment service but do not show change courier card
        var isHideChangeCourierCard: Boolean = false,
        var durationCardDescription: String = "",

        // Courier Selection Error
        var courierSelectionErrorTitle: String? = null,
        var courierSelectionErrorDescription: String? = null,

        // Flag for tracking
        var isHasShownCourierError: Boolean = false,
) : Parcelable {

    val isCustomPinpointError: Boolean
        get() = isDisableChangeCourier && !hasGeolocation

    companion object {
        @JvmStatic
        fun clone(shipmentCartItemModel: ShipmentCartItemModel, cartItemModels: List<CartItemModel>): ShipmentCartItemModel {
            val newShipmentCartItemModel = ShipmentCartItemModel()
            newShipmentCartItemModel.selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
            newShipmentCartItemModel.cartItemModels = cartItemModels
            newShipmentCartItemModel.isAllItemError = shipmentCartItemModel.isAllItemError
            newShipmentCartItemModel.errorTitle = shipmentCartItemModel.errorTitle
            newShipmentCartItemModel.errorDescription = shipmentCartItemModel.errorDescription
            newShipmentCartItemModel.isError = shipmentCartItemModel.isError
            newShipmentCartItemModel.weightUnit = shipmentCartItemModel.weightUnit
            newShipmentCartItemModel.isStateDetailSubtotalViewExpanded = shipmentCartItemModel.isStateDetailSubtotalViewExpanded
            newShipmentCartItemModel.isStateAllItemViewExpanded = shipmentCartItemModel.isStateAllItemViewExpanded
            newShipmentCartItemModel.isStateDropshipperDetailExpanded = shipmentCartItemModel.isStateDropshipperDetailExpanded
            newShipmentCartItemModel.isStateDropshipperHasError = shipmentCartItemModel.isStateDropshipperHasError
            newShipmentCartItemModel.shopName = shipmentCartItemModel.shopName
            newShipmentCartItemModel.shopId = shipmentCartItemModel.shopId
            newShipmentCartItemModel.isProductIsPreorder = shipmentCartItemModel.isProductIsPreorder
            newShipmentCartItemModel.isProductFinsurance = shipmentCartItemModel.isProductFinsurance
            newShipmentCartItemModel.isProductFcancelPartial = shipmentCartItemModel.isProductFcancelPartial
            newShipmentCartItemModel.shopShipmentList = shipmentCartItemModel.shopShipmentList
            newShipmentCartItemModel.orderNumber = shipmentCartItemModel.orderNumber
            newShipmentCartItemModel.isHidingCourier = shipmentCartItemModel.isHidingCourier
            newShipmentCartItemModel.cartString = shipmentCartItemModel.cartString
            newShipmentCartItemModel.shippingId = shipmentCartItemModel.shippingId
            newShipmentCartItemModel.spId = shipmentCartItemModel.spId
            newShipmentCartItemModel.dropshiperName = shipmentCartItemModel.dropshiperName
            newShipmentCartItemModel.dropshiperPhone = shipmentCartItemModel.dropshiperPhone
            newShipmentCartItemModel.isInsurance = shipmentCartItemModel.isInsurance
            newShipmentCartItemModel.isSaveStateFlag = shipmentCartItemModel.isSaveStateFlag
            newShipmentCartItemModel.isStateLoadingCourierState = shipmentCartItemModel.isStateLoadingCourierState
            newShipmentCartItemModel.isStateHasLoadCourierState = shipmentCartItemModel.isStateHasLoadCourierState
            newShipmentCartItemModel.isStateHasExtraMarginTop = shipmentCartItemModel.isStateHasExtraMarginTop
            newShipmentCartItemModel.isBlackbox = shipmentCartItemModel.isBlackbox
            newShipmentCartItemModel.addressId = shipmentCartItemModel.addressId
            newShipmentCartItemModel.isFulfillment = shipmentCartItemModel.isFulfillment
            newShipmentCartItemModel.fulfillmentBadgeUrl = shipmentCartItemModel.fulfillmentBadgeUrl
            newShipmentCartItemModel.fulfillmentId = shipmentCartItemModel.fulfillmentId
            newShipmentCartItemModel.blackboxInfo = shipmentCartItemModel.blackboxInfo
            newShipmentCartItemModel.hasPromoList = shipmentCartItemModel.hasPromoList
            newShipmentCartItemModel.voucherLogisticItemUiModel = shipmentCartItemModel.voucherLogisticItemUiModel
            newShipmentCartItemModel.isLeasingProduct = shipmentCartItemModel.isLeasingProduct
            newShipmentCartItemModel.listPromoCodes = shipmentCartItemModel.listPromoCodes
            newShipmentCartItemModel.shopTypeInfoData = shipmentCartItemModel.shopTypeInfoData
            newShipmentCartItemModel.isDisableChangeCourier = shipmentCartItemModel.isDisableChangeCourier
            newShipmentCartItemModel.isAutoCourierSelection = shipmentCartItemModel.isAutoCourierSelection
            newShipmentCartItemModel.isHideChangeCourierCard = shipmentCartItemModel.isHideChangeCourierCard
            newShipmentCartItemModel.durationCardDescription = shipmentCartItemModel.durationCardDescription
            return newShipmentCartItemModel
        }
    }
}