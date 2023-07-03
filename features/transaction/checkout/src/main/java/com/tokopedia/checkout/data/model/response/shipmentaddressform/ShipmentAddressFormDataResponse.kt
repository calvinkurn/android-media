package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.response.dynamicdata.ShipmentDynamicDataPassing
import com.tokopedia.checkout.data.model.response.egold.EgoldAttributes
import com.tokopedia.purchase_platform.common.feature.coachmarkplus.CoachmarkPlusResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.ImageUploadResponse
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.PopUp
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker

data class ShipmentAddressFormDataResponse(
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("error_code")
    val errorCode: Int = 0,
    @SerializedName("group_address")
    val groupAddress: List<GroupAddress> = emptyList(),
    @SerializedName("kero_token")
    val keroToken: String = "",
    @SerializedName("kero_discom_token")
    val keroDiscomToken: String = "",
    @SerializedName("kero_unix_time")
    val keroUnixTime: Int = 0,
    @SerializedName("pop_up")
    val popup: PopUp = PopUp(),
    @SerializedName("add_on_wording")
    val addOnWording: AddOnWording = AddOnWording(),
    @SerializedName("donation")
    val donation: Donation = Donation(),
    @SerializedName("cod")
    val cod: Cod = Cod(),
    @SerializedName("is_hide_courier_name")
    val hideCourier: Boolean = false,
    @SerializedName("is_blackbox")
    val isBlackbox: Int = 0,
    @SerializedName("egold_attributes")
    val egoldAttributes: EgoldAttributes = EgoldAttributes(),
    @SerializedName("is_show_onboarding")
    val isShowOnboarding: Boolean = false,
    @SerializedName("is_ineligible_promo_dialog_enabled")
    val isIneligiblePromoDialogEnabled: Boolean = false,
    @SerializedName("disabled_features")
    val disabledFeatures: List<String> = emptyList(),
    @SerializedName("tickers")
    val tickers: List<Ticker> = emptyList(),
    @SerializedName("donation_checkbox_status")
    val isDonationCheckboxStatus: Boolean = false,
    @SerializedName("campaign_timer")
    val campaignTimer: CampaignTimer = CampaignTimer(),
    @SerializedName("addresses")
    val addresses: Addresses = Addresses(),
    @SerializedName("promo")
    val promoSAFResponse: PromoSAFResponse = PromoSAFResponse(),
    @SerializedName("open_prerequisite_site")
    val isOpenPrerequisiteSite: Boolean = false,
    @SerializedName("eligible_new_shipping_experience")
    val isEligibleNewShippingExperience: Boolean = false,
    @SerializedName("pop_up_message")
    val popUpMessage: String = "",
    @SerializedName("error_ticker")
    val errorTicker: String = "",
    @SerializedName("cross_sell")
    val crossSell: List<CrossSellResponse> = emptyList(),
    @SerializedName("image_upload")
    val imageUpload: ImageUploadResponse = ImageUploadResponse(),
    @SerializedName("upsell")
    val upsell: Upsell = Upsell(),
    @SerializedName("upsell_v2")
    val newUpsell: NewUpsell = NewUpsell(),
    @SerializedName("cart_data")
    val cartData: String = "",
    @SerializedName("coachmark")
    val coachmark: CoachmarkPlusResponse = CoachmarkPlusResponse(),
    @SerializedName("dynamic_data_passing")
    val dynamicDataPassing: ShipmentDynamicDataPassing = ShipmentDynamicDataPassing(),
    @SerializedName("platform_fee")
    val shipmentPlatformFee: ShipmentPlatformFee = ShipmentPlatformFee(),
    @SerializedName("add_ons_summary")
    val listSummaryAddOns: List<ShipmentSummaryAddOn> = emptyList()
) {

    fun a() {
        val map = hashMapOf<Int, String>()
        for (listSummaryAddOn in listSummaryAddOns) {
            map[listSummaryAddOn.type] = listSummaryAddOn.wording
        }

        // calculate - hashMapOf<typeAddon, Pair<priceAddOn, qtyAddOn>>
        val countMap = hashMapOf<Int, Pair<Double, Int>>()
        countMap[1] = 200000.0 to 1 // jasa pasang
        countMap[2] = 300000.0 to 3 // proteksi

        for (entry in countMap) {
            val text = map[entry.key]!!.replace("qty", entry.value.second.toString()) // Total Jasa Pasang ({{qty}} Jasa)
            val valuetext = map[entry.key]!!.replace("qty", entry.value.first.toString()) // Total Jasa Pasang ({{qty}} Jasa)
        }
    }
}
