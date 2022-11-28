package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 09/06/20
 */

data class GetLayoutResponse(
    @SerializedName("GetSellerDashboardPageLayout")
    val layout: GetSellerDashboardLayout? = GetSellerDashboardLayout(),
    @SerializedName("shopState")
    val shopState: Long = 0
)

data class GetSellerDashboardLayout(
    @SerializedName("widget")
    val widget: List<WidgetModel>? = emptyList()
)