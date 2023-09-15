package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ShopHomeBundleProductUiModel(
        var productId: String = "0",
        var productName: String = "",
        var productImageUrl: String = "",
        var productAppLink: String = "",
        var minOrder: Int? = null
): ImpressHolder()
