package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import kotlinx.parcelize.Parcelize
import java.util.HashMap

@Parcelize
data class ShipmentCartItemModel(
    var isAllItemError: Boolean = false,
    var isError: Boolean = false,
    var errorTitle: String = "",
    val errorDescription: String = "",
    var isHasUnblockingError: Boolean = false,
    var unblockingErrorMessage: String = "",
    var firstProductErrorIndex: Int = -1,
    val isTriggerScrollToErrorProduct: Boolean = false,
    var isCustomEpharmacyError: Boolean = false,

    var shipmentCartData: ShipmentCartData = ShipmentCartData(),
    var selectedShipmentDetailData: ShipmentDetailData? = null,
    val shopShipmentList: List<ShopShipment> = emptyList(),

    // Shop data
    val shopId: Long = 0,
    val shopName: String = "",
    val orderNumber: Int = 0,
    val preOrderInfo: String = "",
    val freeShippingBadgeUrl: String = "",
    val isFreeShippingPlus: Boolean = false, // flag for plus badge tracker
    var shopLocation: String = "",
    val shopAlertMessage: String = "",
    val shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    val isTokoNow: Boolean = false,
    val shopTickerTitle: String = "",
    val shopTicker: String = "",
    val enablerLabel: String = "",

    // AddOns
    val addOnsOrderLevelModel: AddOnGiftingDataModel = AddOnGiftingDataModel(),
    val addOnWordingModel: AddOnGiftingWordingModel = AddOnGiftingWordingModel(),
    val addOnDefaultFrom: String = "",
    val addOnDefaultTo: String = "",

    // Cart item state
    override val cartStringGroup: String,
    val shippingId: Int = 0,
    var spId: Int = 0,
    var boCode: String = "",
    var boUniqueId: String = "",
    var dropshiperName: String = "",
    var dropshiperPhone: String = "",
    val isInsurance: Boolean = false,
    val isSaveStateFlag: Boolean = false,

    val weightUnit: Int = 0,
    val isProductFinsurance: Boolean = false,
    val isProductFcancelPartial: Boolean = false,
    val isProductIsPreorder: Boolean = false,

    var cartItemModels: List<CartItemModel> = ArrayList(),

    // View state
    var isStateDetailSubtotalViewExpanded: Boolean = false,
    var isStateAllItemViewExpanded: Boolean = true,
    val isStateDropshipperDetailExpanded: Boolean = false,
    var isStateDropshipperHasError: Boolean = false,
    var isStateLoadingCourierState: Boolean = false,
    var isStateHasLoadCourierState: Boolean = false,
    var isStateHasLoadCourierTradeInDropOffState: Boolean = false,
    val isStateHasExtraMarginTop: Boolean = false,

    // Flag for courier recommendation
    private val useCourierRecommendation: Boolean = false,
    val isHidingCourier: Boolean = false,

    // for robinhood III
    val isBlackbox: Boolean = false,
    val addressId: String = "",

    val isFulfillment: Boolean = false,
    val fulfillmentBadgeUrl: String = "",
    val fulfillmentId: Long = 0,

    // promo stacking
    var hasPromoList: Boolean = false,
    var voucherLogisticItemUiModel: VoucherLogisticItemUiModel? = null,

    val isLeasingProduct: Boolean = false,
    val bookingFee: Int = 0,
    val listPromoCodes: List<String> = emptyList(),

    val isDropshipperDisable: Boolean = false,
    val isOrderPrioritasDisable: Boolean = false,

    val isHasSetDropOffLocation: Boolean = false,

    // Shipping experiance
    val isEligibleNewShippingExperience: Boolean = false,
    var isTriggerShippingVibrationAnimation: Boolean = false,
    var isShippingBorderRed: Boolean = false,
    val isDisableChangeCourier: Boolean = false,
    val isAutoCourierSelection: Boolean = false,
    val hasGeolocation: Boolean = false,

    // regular shipment service but do not show change courier card
    var isHideChangeCourierCard: Boolean = false,
    var durationCardDescription: String = "",

    // Courier Selection Error
    val courierSelectionErrorTitle: String = "",
    val courierSelectionErrorDescription: String = "",

    // Flag for tracking
    var isHasShownCourierError: Boolean = false,

    // Schedule delivery
    var isShowScheduleDelivery: Boolean = false,
    var scheduleDate: String = "",
    var timeslotId: Long = 0L,
    var validationMetadata: String = "",
    val ratesValidationFlow: Boolean = false,
    var hasSentScheduleDeliveryAnalytics: Boolean = false,

    // Multiple Order Plus Coachmark
    var coachmarkPlus: CoachmarkPlusData = CoachmarkPlusData(),

    // Epharmacy
    var hasEthicalProducts: Boolean = false,
    var hasNonEthicalProducts: Boolean = false,
    var prescriptionIds: List<String> = emptyList(),
    var tokoConsultationId: String = "",
    var partnerConsultationId: String = "",
    var consultationDataString: String = "",
    var shouldResetCourier: Boolean = false,

    // new owoc
    val groupType: Int = 0,
    val uiGroupType: Int = 0,
    val groupInfoName: String = "",
    val groupInfoBadgeUrl: String = "",
    val groupInfoDescription: String = "",
    val groupInfoDescriptionBadgeUrl: String = "",

    // add ons subtotal
    var subtotalAddOnMap: HashMap<Int, String> = hashMapOf()
) : Parcelable, ShipmentCartItem {

    val isCustomPinpointError: Boolean
        get() = isDisableChangeCourier && !hasGeolocation

    val cartItemModelsGroupByOrder: Map<String, List<CartItemModel>>
        get() = cartItemModels.filter { !it.isError }
            .groupBy { it.cartStringOrder }
}
