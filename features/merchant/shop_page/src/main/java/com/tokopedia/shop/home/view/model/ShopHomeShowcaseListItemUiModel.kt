package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.model.ImpressHolder

data class ShopHomeShowcaseListItemUiModel(
    var id: String = "0",
    var imageUrl: String = "",
    var appLink: String = "",
    var name: String = "",
    var viewType: String = "",
    var isShowEtalaseName: Boolean = true
) : ImpressHolder()
