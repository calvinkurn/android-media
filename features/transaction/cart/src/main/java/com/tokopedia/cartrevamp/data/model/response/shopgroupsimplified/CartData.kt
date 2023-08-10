package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartrevamp.data.model.response.promo.CartPromoData
import com.tokopedia.purchase_platform.common.feature.coachmarkplus.CoachmarkPlusResponse
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker

data class CartData(
    @SerializedName("tickers")
    val tickers: List<Ticker> = ArrayList(),
    @SerializedName("max_char_note")
    val maxCharNote: Int = 0,
    @SerializedName("messages")
    val messages: Messages = Messages(),
    @SerializedName("global_checkbox_state")
    val isGlobalCheckboxState: Boolean = false,
    @SerializedName("promo")
    val promo: CartPromoData = CartPromoData(),
    @SerializedName("out_of_service")
    val outOfService: OutOfService = OutOfService(),
    @SerializedName("shopping_summary")
    val shoppingSummary: ShoppingSummary = ShoppingSummary(),
    @SerializedName("promo_summary")
    val promoSummary: PromoSummary = PromoSummary(),
    @SerializedName("toko_cabang")
    val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo(),
    @SerializedName("available_section")
    val availableSection: AvailableSection = AvailableSection(),
    @SerializedName("unavailable_section")
    val unavailableSections: List<UnavailableSection> = emptyList(),
    @SerializedName("unavailable_section_action")
    val unavailableSectionAction: List<Action> = emptyList(),
    @SerializedName("localization_choose_address")
    val localizationChooseAddress: LocalizationChooseAddress = LocalizationChooseAddress(),
    @SerializedName("pop_up_message")
    val popUpMessage: String = "",
    @SerializedName("popup_error_message")
    val popupErrorMessage: String = "",
    @SerializedName("placeholder_note")
    val placeholderNote: String = "",
    @SerializedName("coachmark")
    val coachmark: CoachmarkPlusResponse = CoachmarkPlusResponse(),
    @SerializedName("show_bundle_price")
    val showBundlePrice: Boolean = false
)
