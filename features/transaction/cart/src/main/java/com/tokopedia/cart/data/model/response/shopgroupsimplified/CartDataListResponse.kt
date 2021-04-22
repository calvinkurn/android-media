package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.purchase_platform.common.feature.fulfillment.response.TokoCabangInfo
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import java.util.*

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class CartDataListResponse(
        @SerializedName("errors")
        val errors: List<String> = ArrayList(),
        @SerializedName("tickers")
        val tickers: List<Ticker> = ArrayList(),
        @SerializedName("is_coupon_active")
        val isCouponActive: Int = 0,
        @SerializedName("max_quantity")
        val maxQuantity: Int = 0,
        @SerializedName("max_char_note")
        val maxCharNote: Int = 0,
        @SerializedName("messages")
        val messages: Messages = Messages(),
        @SerializedName("donation")
        val donation: Donation = Donation(),
        @SerializedName("global_checkbox_state")
        val isGlobalCheckboxState: Boolean = false,
        @SerializedName("promo")
        val promo: CartPromoData = CartPromoData(),
        @SerializedName("empty_cart")
        val emptyCart: EmptyCart = EmptyCart(),
        @SerializedName("out_of_service")
        val outOfService: OutOfService = OutOfService(),
        @SerializedName("shopping_summary")
        val shoppingSummary: ShoppingSummary = ShoppingSummary(),
        @SerializedName("promo_summary")
        val promoSummary: PromoSummary = PromoSummary(),
        @SerializedName("fulfillment_message")
        val fulfillmentMessage: String = "",
        @SerializedName("toko_cabang")
        val tokoCabangInfo: TokoCabangInfo = TokoCabangInfo(),
        @SerializedName("available_section")
        val availableSection: AvailableSection = AvailableSection(),
        @SerializedName("unavailable_ticker")
        val unavailableTicker: String = "",
        @SerializedName("unavailable_section")
        val unavailableSections: List<UnavailableSection> = emptyList(),
        @SerializedName("unavailable_section_action")
        val unavailableSectionAction: List<Action> = emptyList(),
        @SerializedName("total_product_error")
        val totalProductError: Int = 0,
        @SerializedName("localization_choose_address")
        val localizationChooseAddress: LocalizationChooseAddress = LocalizationChooseAddress(),
        @SerializedName("pop_up_message")
        val popUpMessage: String = ""
)