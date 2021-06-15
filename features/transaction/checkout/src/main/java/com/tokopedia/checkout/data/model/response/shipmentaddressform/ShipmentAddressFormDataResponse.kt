package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.response.egold.EgoldAttributes
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
        @SerializedName("disabled_features_detail")
        val disabledFeaturesDetail: DisabledFeaturesDetail = DisabledFeaturesDetail(),
        @SerializedName("promo")
        val promoSAFResponse: PromoSAFResponse = PromoSAFResponse(),
        @SerializedName("open_prerequisite_site")
        val isOpenPrerequisiteSite: Boolean = false,
        @SerializedName("eligible_new_shipping_experience")
        val isEligibleNewShippingExperience: Boolean = false,
        @SerializedName("pop_up_message")
        val popUpMessage: String = ""
)