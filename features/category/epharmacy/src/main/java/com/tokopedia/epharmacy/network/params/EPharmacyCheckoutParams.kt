package com.tokopedia.epharmacy.network.params

import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse

data class EPharmacyCheckoutParams(
    var businessId: String = "3da11b0a-da5c-461f-8e82-14dfd3bf238d",
    var productId: String = "ECONSUL",
    var productQuantity: Int = 1,
    var shopId: String = "",
    var note: String = "",
    var flowType: String? = "INSTANT",
    var checkoutBusinessType: Int? = 0,
    var checkoutDataType: String? = "ECONSUL_ATC_INSTANT",
    var price: String? = "",
    var source: String = "econsulcheckout",
    var ePharmacyCheckoutCartGroup : EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.CartGroup?
)
