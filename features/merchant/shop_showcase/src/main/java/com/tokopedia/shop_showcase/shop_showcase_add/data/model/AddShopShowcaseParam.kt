package com.tokopedia.shop_showcase.shop_showcase_add.data.model

data class AddShopShowcaseParam(
        var name: String = "",
        var productIDs: MutableList<String> = mutableListOf()
)