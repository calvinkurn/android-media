package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyConsultationInfoResponse

data class AvailableGroup(
    @SerializedName("user_address_id")
    val userAddressId: String = "",
    @SerializedName("shipment_information")
    val shipmentInformation: ShipmentInformation = ShipmentInformation(),
    @SerializedName("cart_string")
    val cartString: String = "",
    @SerializedName("pinned")
    val pinned: Pinned = Pinned(),
    @SerializedName("add_on")
    val giftingAddOn: GiftingAddOn = GiftingAddOn(),
    @SerializedName("epharmacy_consultation")
    val epharmacyConsultationInfo: EpharmacyConsultationInfoResponse = EpharmacyConsultationInfoResponse(),
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @SerializedName("is_fulfillment_service")
    val isFulfillment: Boolean = false,
    @SerializedName("warehouse")
    val warehouse: Warehouse = Warehouse(),
    @SerializedName("cart_details")
    val cartDetails: List<CartDetail> = emptyList(),
    @SerializedName("total_cart_details_error")
    val totalCartDetailsError: Int = 0,
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("sort_key")
    val sortKey: Long = 0,
    @SerializedName("has_promo_list")
    val hasPromoList: Boolean = false,
    @SerializedName("checkbox_state")
    val checkboxState: Boolean = false,
    @SerializedName("promo_codes")
    val promoCodes: List<String> = emptyList(),
    @SerializedName("bo_metadata")
    val boMetadata: BoMetadata = BoMetadata()
)
