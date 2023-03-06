package com.tokopedia.promocheckoutmarketplace.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.promocheckoutmarketplace.data.response.AdditionalBoData
import com.tokopedia.promocheckoutmarketplace.data.response.BenefitDetail
import com.tokopedia.promocheckoutmarketplace.data.response.BoClashingInfo
import com.tokopedia.promocheckoutmarketplace.data.response.ClashingInfo
import com.tokopedia.promocheckoutmarketplace.data.response.PromoCoachmark
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.presentation.adapter.PromoCheckoutAdapterTypeFactory

data class PromoListItemUiModel(
    var id: String = "",
    var uiData: UiData,
    var uiState: UiState
) : Visitable<PromoCheckoutAdapterTypeFactory> {

    override fun type(typeFactory: PromoCheckoutAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    // get promo code for gql request / response
    // because bebas ongkir can have multiple promo code in `boAdditionalData`
    // for ui model usage please use `promoListItemUiModel.uiData.promoCode`
    fun getPromoCode(listOfPromoCodes: Collection<String>): String {
        return if (uiState.isBebasOngkir) {
            uiData.boAdditionalData.map { it.code }.intersect(listOfPromoCodes).firstOrNull() ?: ""
        } else {
            uiData.promoCode
        }
    }

    data class UiData(
        var promoId: String = "",
        var uniqueId: String = "",
        var shopId: Int = 0,
        var parentIdentifierId: Int = 0,
        var title: String = "",
        var currencyDetailStr: String = "",

        var errorMessage: String = "",
        var errorIcon: String = "",
        var benefitAmount: Int = 0,
        var promoCode: String = "",
        var couponAppLink: String = "",
        var coachMark: PromoCoachmark = PromoCoachmark(),
        // Store clashing info data from backend.
        // This should not be changed. Initialize once after get data response
        var clashingInfos: List<ClashingInfo> = emptyList(),
        // Store current applied promo causing this promo clash and can't be selected, based on data from #clashingInfo
        var currentClashingPromo: MutableList<String> = mutableListOf(),
        var promoInfos: List<PromoInfo> = emptyList(),
        var remainingPromoCount: Int = 0,
        var tabId: String = "",
        var shippingOptions: String = "",
        var paymentOptions: String = "",
        var benefitDetail: BenefitDetail = BenefitDetail(),

        // fields related to bebas ongkir promo

        // Store clashing info with BO
        // When user choose promo that clashes with BO, info will be shown in bottomsheet
        var boClashingInfos: List<BoClashingInfo> = emptyList(),
        // Store BO promo data
        // When user choose BO promo, get unique id and promo code from here
        var boAdditionalData: List<AdditionalBoData> = emptyList()
    )

    data class UiState(
        var isParentEnabled: Boolean = false,
        var isSelected: Boolean = false,
        var isPreSelected: Boolean = false,
        var isAttempted: Boolean = false,
        var isCausingOtherPromoClash: Boolean = false,
        var isRecommended: Boolean = false,
        var isDisabled: Boolean = false,
        var isHighlighted: Boolean = false,
        var isUpdateSelectionStateAction: Boolean = false,
        var isLastPromoItem: Boolean = false,
        var isBebasOngkir: Boolean = false
    )
}
