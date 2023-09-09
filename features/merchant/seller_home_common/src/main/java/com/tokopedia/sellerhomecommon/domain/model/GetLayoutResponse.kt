package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 09/06/20
 */

data class GetLayoutResponse(
    @SerializedName("GetSellerDashboardPageLayout")
    val layout: GetSellerDashboardLayout = GetSellerDashboardLayout()
)

data class GetSellerDashboardLayout(
    @SerializedName("widget")
    val widget: List<WidgetModel>? = emptyList(),
    @SerializedName("tabs")
    val tabs: List<TabModel>? = emptyList(),
    @SerializedName("shopState")
    val shopState: Long = 0,
    @SerializedName("personaStatus")
    val personaStatus: Int? = 0
)
