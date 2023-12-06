package com.tokopedia.content.product.preview.view.uimodel

/**
 * @author by astidhiyaa on 06/12/23
 */
data class ReportUiModel(
    val text: String, //change into @StringRes
    val reasonCode: Int,
) {
    companion object {
        val Empty
            get() = ReportUiModel(
                text = "",
                reasonCode = 0,
            )
    }
}
