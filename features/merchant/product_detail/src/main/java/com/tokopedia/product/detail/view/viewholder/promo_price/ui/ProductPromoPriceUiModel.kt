package com.tokopedia.product.detail.view.viewholder.promo_price.ui

import com.tokopedia.product.detail.common.data.model.pdplayout.Price
import com.tokopedia.product.detail.common.data.model.promoprice.PromoPriceUiModel

data class ProductPromoPriceUiModel(
    val type: String = "",
    val name: String = "",
    var priceComponentType: Int = 0,
    var promoPriceData: PromoPriceUiModel? = null,
    var normalPromoUiModel: Price? = null,
    var boLogo: String = ""
)

