package com.tokopedia.sellerhome.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetLayoutResponse(
        @Expose
        @SerializedName("GetSellerDashboardLayout")
        val layout: GetSellerDashboardLayout?
)

data class GetSellerDashboardLayout(
        @Expose
        @SerializedName("widget")
        val widget: List<WidgetModel>?
)