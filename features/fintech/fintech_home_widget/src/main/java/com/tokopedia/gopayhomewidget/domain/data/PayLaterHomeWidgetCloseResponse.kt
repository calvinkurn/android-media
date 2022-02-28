package com.tokopedia.gopayhomewidget.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PayLaterHomeWidgetCloseResponse(

    @SerializedName("paylater_getHomeWidget")
    val paylaterCloseSuccessData: PayLaterCloseSuccessResponse
)

data class PayLaterCloseSuccessResponse (
    @SerializedName("success")
    val closeSuccessStatus:Boolean
)
