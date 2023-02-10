package com.tokopedia.report.view.fragment.models

import androidx.compose.runtime.Stable
import com.tokopedia.common_compose.principles.UiText
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Stable
data class ProductReportUiState(
    val title: UiText = UiText.ResourceText(R.string.product_report_header),
    val allData: List<ProductReportReason> = emptyList(),
    val data: List<ProductReportReason> = emptyList(),
    val baseParent: ProductReportReason? = null,
    val filterId: List<Int> = emptyList(),
    val error: String? = null
) {

    fun isSubtitleVisible(reason: ProductReportReason): Boolean {
        return reason.detail.isNotBlank() && filterId.isNotEmpty()
    }
}
