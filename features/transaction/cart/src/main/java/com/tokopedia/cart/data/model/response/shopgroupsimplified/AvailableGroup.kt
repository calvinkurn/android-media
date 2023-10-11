package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyConsultationInfoResponse

data class AvailableGroup(
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
    @SerializedName("is_fulfillment_service")
    val isFulfillment: Boolean = false,
    @SerializedName("warehouse")
    val warehouse: Warehouse = Warehouse(),
    @SerializedName("checkbox_state")
    val checkboxState: Boolean = false,
    @SerializedName("promo_codes")
    val promoCodes: List<String> = emptyList(),
    @SerializedName("bo_metadata")
    val boMetadata: BoMetadata = BoMetadata(),
    @SerializedName("group_type")
    val groupType: Int = 0,
    @SerializedName("ui_group_type")
    val uiGroupType: Int = 0,
    @SerializedName("group_information")
    val groupInformation: GroupInformation = GroupInformation(),
    @SerializedName("group_shop_v2_cart")
    val groupShopCartData: List<GroupShopCart> = emptyList()
) {
    fun isUsingOWOCDesign(): Boolean = uiGroupType == UI_GROUP_TYPE_OWOC

    fun isTypeOWOC(): Boolean = groupType == GROUP_TYPE_OWOC

    companion object {
        private const val UI_GROUP_TYPE_OWOC = 1
        private const val GROUP_TYPE_OWOC = 2
    }
}
