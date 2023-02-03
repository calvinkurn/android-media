package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName

data class ShopLayoutWidgetV2(
    @SerializedName("widgets")
    val listWidget: List<ShopLayoutWidget.Widget> = listOf()

) {
    data class Response(
        @SerializedName("shopPageGetLayoutV2")
        val shopLayoutWidgetV2: ShopLayoutWidgetV2 = ShopLayoutWidgetV2()
    )
}
