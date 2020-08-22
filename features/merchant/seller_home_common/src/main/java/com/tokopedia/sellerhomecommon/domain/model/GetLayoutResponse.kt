package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 09/06/20
 */

data class GetLayoutResponse(
        @Expose
        @SerializedName("GetSellerDashboardPageLayout")
        val layout: GetSellerDashboardLayout?
)

data class GetSellerDashboardLayout(
        @Expose
        @SerializedName("widget")
        val widget: List<WidgetModel>?
)