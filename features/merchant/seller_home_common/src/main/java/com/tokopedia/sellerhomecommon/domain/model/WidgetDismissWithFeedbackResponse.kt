package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 02/09/22.
 */

data class WidgetDismissWithFeedbackResponse(
    @SerializedName("dashboardDismissWithFeedback")
    val data: WidgetDismissWithFeedbackModel = WidgetDismissWithFeedbackModel()
)

data class WidgetDismissWithFeedbackModel(
    @SerializedName("error")
    val isError: Boolean = false,
    @SerializedName("errorMsg")
    val errorMsg: String = String.EMPTY,
    @SerializedName("dismissToken")
    val dismissToken: String = String.EMPTY
)