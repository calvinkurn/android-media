package com.tokopedia.cmhomewidget.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DeleteCMHomeWidgetDataGqlResponse(
    @SerializedName("notifier_dismissHtdw")
    @Expose
    val deleteCMHomeWidgetDataResponse: DeleteCMHomeWidgetDataResponse
)

data class DeleteCMHomeWidgetDataResponse(
    @SerializedName("status")
    @Expose
    val status: Int
)