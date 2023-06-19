package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.SummaryAddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData

data class OrderCart(
    var cartString: String = "",
    var paymentProfile: String = "",
    var products: MutableList<OrderProduct> = ArrayList(),
    var shop: OrderShop = OrderShop(),
    var kero: OrderKero = OrderKero(),
    var addOnWordingData: AddOnWordingData = AddOnWordingData(),
    var cartData: String = "",
    var summaryAddOnsProduct: List<SummaryAddOnProductDataModel> = mutableListOf()
)
