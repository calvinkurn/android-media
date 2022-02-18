package com.tokopedia.gopayhomewidget.presentation.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPayLaterWidgetDataGqlResponse (
    @SerializedName("paylater_getHomeWidget")
    @Expose
    val payLaterWidgetData: PayLaterWidgetData
)

class PayLaterWidgetData {

}
