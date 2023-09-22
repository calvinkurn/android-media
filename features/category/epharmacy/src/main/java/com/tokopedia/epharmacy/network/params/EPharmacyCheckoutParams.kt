package com.tokopedia.epharmacy.network.params

import com.tokopedia.epharmacy.network.response.EPharmacyAtcInstantResponse

data class EPharmacyCheckoutParams(
    var businessId: String = "8811b325f-d0cf-4ba9-8d0a-3bce7e8c96c0",
    var productId: String = "ECONSUL",
    var productQuantity: Int = 1,
    var shopId: String = "",
    var note: String = "",
    var flowType: String? = "",
    var checkoutBusinessType: Int? = 0,
    var checkoutDataType: String? = "",
    var price: String? = "",
    var source: String = "econsulcheckout",
    var ePharmacyCheckoutCartGroup : EPharmacyAtcInstantResponse.CartGeneralAddToCartInstant.CartGeneralAddToCartInstantData.BusinessDataList.BusinessData.CartGroup?
)
