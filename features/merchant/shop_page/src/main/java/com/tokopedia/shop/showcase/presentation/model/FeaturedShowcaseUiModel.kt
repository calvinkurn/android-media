package com.tokopedia.shop.showcase.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder

data class FeaturedShowcaseUiModel(
        var id: String = "",
        var name: String = "",
        var count: Int = 0,
        var imageUrl: String = ""
): ImpressHolder()