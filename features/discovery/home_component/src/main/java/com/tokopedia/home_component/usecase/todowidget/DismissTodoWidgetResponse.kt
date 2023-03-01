package com.tokopedia.home_component.usecase.todowidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by frenzel
 */
data class DismissTodoWidgetResponse(
    @SerializedName("closeMultisource")
    @Expose
    val closeMultisource: CloseMultisource = CloseMultisource()
) {
    data class CloseMultisource(
        @SerializedName("success")
        @Expose
        val isSuccess: Boolean = false,
        @SerializedName("message")
        @Expose
        val message: String = ""
    )
}
