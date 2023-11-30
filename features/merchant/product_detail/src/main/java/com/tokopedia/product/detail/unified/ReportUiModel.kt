package com.tokopedia.product.detail.unified

/**
 * @author by astidhiyaa on 28/11/23
 */
data class ReportUiModel(
    val text: String, //change into @StringRes
    val isSelected: Boolean, //delete?
    val reasonCode: Int,
) {
    companion object {
        val Empty
            get() = ReportUiModel(
                text = "",
                isSelected = false,
                reasonCode = 0,
            )
    }
}
