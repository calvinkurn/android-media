package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

sealed class ViewToViewATCStatus {
    abstract val message: String
    data class Success(
        override val message: String
    ): ViewToViewATCStatus()

    data class Failure(
        override val message: String
    ): ViewToViewATCStatus()
}
