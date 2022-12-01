package com.tokopedia.shop.common.widget.bundle.model

import com.tokopedia.kotlin.model.ImpressHolder

data class BundleProductUiModel(
        var productId: String = "0",
        var productName: String = "",
        var productImageUrl: String = "",
        var productAppLink: String = "",
        var hasVariant: Boolean = false
): ImpressHolder()