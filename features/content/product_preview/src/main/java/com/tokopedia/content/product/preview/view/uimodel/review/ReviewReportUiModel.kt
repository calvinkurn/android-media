package com.tokopedia.content.product.preview.view.uimodel.review

/**
 * @author by astidhiyaa on 06/12/23
 */
data class ReviewReportUiModel(
    val text: String,
    val reasonCode: Int,
) {
    companion object {
        val Empty
            get() = ReviewReportUiModel(
                text = "",
                reasonCode = 0,
            )
    }
}
