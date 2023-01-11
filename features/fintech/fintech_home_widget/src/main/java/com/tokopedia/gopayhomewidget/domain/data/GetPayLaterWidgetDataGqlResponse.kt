package com.tokopedia.gopayhomewidget.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPayLaterWidgetDataGqlResponse(
    @SerializedName("paylater_getHomeWidget")
    @Expose
    val payLaterWidgetData: PayLaterWidgetData
)

data class PayLaterWidgetData(
    @SerializedName("show")
    @Expose
    val isShow: Boolean?,
    @SerializedName("image_dark")
    @Expose
    val imageDark: String?,
    @SerializedName("image_light")
    @Expose
    val imageLight: String?,

    @SerializedName("case_type")
    @Expose
    val caseType: Int?,

    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("button")
    @Expose
    val button: PayLaterButton?,

    @SerializedName("gateway_code")
    val gatewayCode: String?
)

data class PayLaterButton(
    @SerializedName("button_name")
    @Expose
    val buttonName: String?,
    @SerializedName("cta_type")
    @Expose
    var ctaType: Int? = 2,
    @SerializedName("web_url")
    @Expose
    val webUrl: String?,
    @SerializedName("apps_url")
    @Expose
    val appsUrl: String?
)
