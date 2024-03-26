package com.tokopedia.epharmacy.network.params

import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse
import com.tokopedia.url.TokopediaUrl

data class EPharmacyCheckoutParams(
    var productId: String = "ECONSUL",
    var productQuantity: Int = 1,
    var shopId: String = "",
    var note: String = "",
    var flowType: String? = "INSTANT",
    var checkoutBusinessType: Int = 49,
    var checkoutDataType: String? = "ECONSUL_ATC_INSTANT",
    var price: String? = "",
    var source: String = "econsulcheckout",
    var ePharmacyCheckoutCartGroup: EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.CartGroup?
) {
    val businessId: String
        get() = if (TokopediaUrl.getInstance().GQL.contains("staging")) "3da11b0a-da5c-461f-8e82-14dfd3bf238d" else "812b1654-4f58-41af-9f47-f04475aab6ee"
}
