package com.tokopedia.shop.common.view.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ShopEtalaseUiModel(
        var id: String = "",
        var name: String = "",
        var count: Int = 0,
        var imageUrl: String? = "",
        var type: Int = 1
): ImpressHolder()