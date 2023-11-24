package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse

data class GroupShop(
    @SerializedName("group_type")
    val groupType: Int = 0,
    @SerializedName("ui_group_type")
    val uiGroupType: Int = 0,
    @SerializedName("group_information")
    val groupInformation: GroupInformation = GroupInformation(),
    @SerializedName("group_shop_v2_saf")
    val groupShopV2: List<GroupShopV2> = emptyList(),
    @SerializedName("add_ons")
    val addOns: AddOnGiftingResponse = AddOnGiftingResponse(),
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("errors_unblocking")
    val unblockingErrors: List<String> = emptyList(),
    @SerializedName("shop_shipments")
    val shopShipments: List<ShopShipment> = emptyList(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    val shippingId: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    val spId: Int = 0,
    @SerializedName("bo_code")
    val boCode: String = "",
    @SerializedName("dropshipper")
    val dropshiper: Dropshiper = Dropshiper(),
    @SerializedName("is_insurance")
    val isInsurance: Boolean = false,
    @SerializedName("is_fulfillment_service")
    val isFulfillment: Boolean = false,
    @SerializedName("toko_cabang")
    val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo(),
    @SerializedName("warehouse")
    val warehouse: Warehouse = Warehouse(),
    @SerializedName("cart_string")
    val cartString: String = "",
    @SerializedName("has_promo_list")
    val isHasPromoList: Boolean = false,
    @SerializedName("save_state_flag")
    val isSaveStateFlag: Boolean = false,
    @SerializedName("vehicle_leasing")
    val vehicleLeasing: VehicleLeasing = VehicleLeasing(),
    @SerializedName("promo_codes")
    val listPromoCodes: List<String> = emptyList(),
    @SerializedName("shipment_information")
    val shipmentInformation: ShipmentInformation = ShipmentInformation(),
    @SerializedName("is_disable_change_courier")
    val isDisableChangeCourier: Boolean = false,
    @SerializedName("auto_courier_selection")
    val autoCourierSelection: Boolean = false,
    @SerializedName("bo_metadata")
    val boMetadata: BoMetadata = BoMetadata(),
    @SerializedName("courier_selection_error")
    val courierSelectionError: CourierSelectionError = CourierSelectionError(),
    @SerializedName("scheduled_delivery")
    val scheduledDelivery: ScheduleDelivery = ScheduleDelivery(),
    @SerializedName("rates_validation_flow")
    val ratesValidationFlow: Boolean = false,
    @SerializedName("subtotal_add_ons")
    val listSubtotalAddOns: List<SubtotalAddOn> = emptyList(),
    // new O2O
    @SerializedName("group_metadata")
    val groupMetadata: String = ""
)
