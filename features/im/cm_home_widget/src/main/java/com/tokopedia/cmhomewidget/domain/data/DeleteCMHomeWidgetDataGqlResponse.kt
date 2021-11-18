package com.tokopedia.cmhomewidget.domain.data

import com.google.gson.annotations.SerializedName

data class DeleteCMHomeWidgetDataGqlResponse(
    @SerializedName("notifier_dismissHtdw")
    val deleteCMHomeWidgetDataResponse: DeleteCMHomeWidgetDataResponse
)

data class DeleteCMHomeWidgetDataResponse(
    @SerializedName("status")
    val status: Int
)