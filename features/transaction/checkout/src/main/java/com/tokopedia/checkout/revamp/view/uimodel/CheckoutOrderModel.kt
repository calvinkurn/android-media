package com.tokopedia.checkout.revamp.view.uimodel

import android.os.Parcelable
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentAction
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import kotlinx.parcelize.Parcelize

data class CheckoutOrderModel(
    override val cartStringGroup: String,
    var isAllItemError: Boolean = false,
    var isError: Boolean = false,
    var errorTitle: String = "",
    val errorDescription: String = "",
    var isHasUnblockingError: Boolean = false,
    var unblockingErrorMessage: String = "",
    var firstProductErrorIndex: Int = -1,
    val isTriggerScrollToErrorProduct: Boolean = false,
    var isCustomEpharmacyError: Boolean = false,

    val shipment: CheckoutOrderShipment = CheckoutOrderShipment(),

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
    val shopShipmentList: List<ShopShipment> = emptyList(),
    val isTokoNow: Boolean = false,
    val shopTickerTitle: String = "",
    var shopTicker: String = "",
    val enablerLabel: String = "",

    // AddOns
    val addOnsOrderLevelModel: AddOnGiftingDataModel = AddOnGiftingDataModel(),
    val addOnWordingModel: AddOnGiftingWordingModel = AddOnGiftingWordingModel(),
    val addOnDefaultFrom: String = "",
    val addOnDefaultTo: String = "",

    // Cart item state
    val shippingId: Int = 0,
    var spId: Int = 0,
    var boCode: String = "",
    var boUniqueId: String = "",

    // dropship
    var useDropship: Boolean = false,
    var isDropshipperDisabled: Boolean = false,
    var stateDropship: CheckoutDropshipWidget.State = CheckoutDropshipWidget.State.GONE,
    var dropshipName: String = "",
    var dropshipPhone: String = "",
    var isDropshipNameValid: Boolean = false,
    var isDropshipPhoneValid: Boolean = false,

    val isInsurance: Boolean = false,
    val isSaveStateFlag: Boolean = false,

    val weightUnit: Int = 0,
    val isProductFinsurance: Boolean = false,
    val isProductFcancelPartial: Boolean = false,
    val isProductIsPreorder: Boolean = false,

    // Do not use this variable, except for checkout param
    private val finalCheckoutProducts: List<CheckoutProductModel> = ArrayList(),
    val orderProductIds: List<String> = emptyList(),
    var preOrderDurationDay: Int = 0,

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

    val postalCode: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val districtId: String = "",
    val keroToken: String = "",
    val keroUnixTime: String = "",
    val boMetadata: BoMetadata = BoMetadata(),

    // promo stacking
    var hasPromoList: Boolean = false,

    val isLeasingProduct: Boolean = false,
    val bookingFee: Int = 0,
    val listPromoCodes: List<String> = emptyList(),

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
    val shippingComponents: ShippingComponents = ShippingComponents.RATES,
    var hasSentScheduleDeliveryAnalytics: Boolean = false,
    val startDate: String = "",
    val isRecommendScheduleDelivery: Boolean = false,

    // OFOC
    val groupingState: Int = 0,

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
    var subtotalAddOnMap: HashMap<Int, String> = hashMapOf(),

    // O2O
    val groupMetadata: String = "",

    // ofoc
    val shipmentAction: HashMap<Long, ShipmentAction> = HashMap()
) : CheckoutItem {

    val isCustomPinpointError: Boolean
        get() = isDisableChangeCourier && !hasGeolocation

    val finalCheckoutProductsGroupByOrder: Map<String, List<CheckoutProductModel>>
        get() = finalCheckoutProducts.filter { !it.isError }
            .groupBy { it.cartStringOrder }

    val hasValidEthicalDrugProduct: Boolean
        get() = finalCheckoutProducts.find { !it.isError && it.ethicalDrugDataModel.needPrescription } != null

    val isEnableDropship: Boolean
        get() = !isDropshipperDisabled && shipment.courierItemData?.isAllowDropshiper == true &&
            shipment.courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()
}

@Parcelize
data class CoachmarkPlusData(
    val isShown: Boolean = false,
    val title: String = "",
    val content: String = ""
) : Parcelable

data class CheckoutOrderShipment(
    val isLoading: Boolean = false,
    val courierItemData: CourierItemData? = null,
    val shippingCourierUiModels: List<ShippingCourierUiModel> = emptyList(),
    val logisticPromoViewModel: LogisticPromoUiModel? = null,
    val logisticPromoShipping: ShippingCourierUiModel? = null,
    val isApplyLogisticPromo: Boolean = false,
    val insurance: CheckoutOrderInsurance = CheckoutOrderInsurance(),
    val isHideChangeCourierCard: Boolean = false,

    // Analytics
    var hasTriggerViewMessageTracking: Boolean = false
)

data class CheckoutOrderInsurance(
    var isCheckInsurance: Boolean = false
)

enum class ShippingComponents(val value: Int) {
    SCHELLY_WITH_RATES(1),
    SCHELLY(2),
    RATES(3);

    companion object {
        fun fromInt(value: Int) = ShippingComponents.values().firstOrNull { it.value == value } ?: RATES
    }
}
