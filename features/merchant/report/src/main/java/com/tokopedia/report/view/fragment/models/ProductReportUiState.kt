package com.tokopedia.report.view.fragment.models

import androidx.compose.runtime.Stable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.report.R
import com.tokopedia.report.data.model.ProductReportReason
import com.tokopedia.report.view.fragment.unify_components.UiText

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Stable
data class ProductReportUiState(
    val data: List<ProductReportReason> = emptyList(),
    val baseParent: ProductReportReason? = null,
    val filterId: List<Int> = emptyList(),
    val error: String? = null
) {

    val title: UiText
        get() = if (filterId.isEmpty()) UiText.ResourceText(R.string.product_report_header)
        else {
            val id = filterId.lastOrNull() ?: -1
            val reason = data.firstOrNull {
                it.categoryId.toIntOrZero() == id
            }
            UiText.StringText(reason?.value.orEmpty())
        }

    private val filterReason = mutableListOf<ProductReportReason>()

    fun getFilterReason(): List<ProductReportReason> {
        filterReason.clear()
        val id = filterId.lastOrNull() ?: -1

        val reason = if (id <= 0) {
            data
        } else {
            val reason = data.firstOrNull {
                it.categoryId.toIntOrZero() == id
            }
            reason?.children.orEmpty()
        }
        filterReason.addAll(reason)

        return filterReason
    }

    fun isSubtitleVisible(reason: ProductReportReason): Boolean {
        return reason.detail.isNotBlank() && filterId.isNotEmpty()
    }
}