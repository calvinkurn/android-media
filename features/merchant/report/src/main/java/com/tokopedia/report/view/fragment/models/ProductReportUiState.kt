package com.tokopedia.report.view.fragment.models

import androidx.compose.runtime.Stable
import com.tokopedia.nest.principles.utils.UiText
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Stable
data class ProductReportUiState(
    val title: UiText = UiText.Resource(R.string.product_report_header),
    val allData: List<ProductReportReason> = emptyList(),
    val data: List<ProductReportReason> = emptyList(),
    val baseParent: ProductReportReason? = null,
    val filterId: List<Int> = emptyList()
) {

    fun isSubtitleVisible(reason: ProductReportReason): Boolean {
        return reason.detail.isNotBlank() && filterId.isNotEmpty()
    }
}
