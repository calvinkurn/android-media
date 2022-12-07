package com.tokopedia.productbundlewidget.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.product_bundle.common.data.model.request.Bundle

data class GetBundleParam(
    @SerializedName("productId") val productId: String = "",
    @SerializedName("warehouseId") val warehouseId: String = "",
    @SerializedName("shopId") val shopId: String = "",
    @SerializedName("bundleList") val bundleList: List<Bundle> = emptyList(),
    @SerializedName("widgetType") val widgetType: WidgetType = WidgetType.TYPE_1,
    @SerializedName("pageSource") val pageSource: String = ""
)
