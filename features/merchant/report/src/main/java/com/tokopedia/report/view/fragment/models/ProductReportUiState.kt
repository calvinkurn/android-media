package com.tokopedia.report.view.fragment.models

import androidx.compose.runtime.Stable
import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 07/09/22"
 * Project name: android-tokopedia-core
 **/

@Stable
data class ProductReportUiState(
    val data: List<ProductReportReason> = emptyList(),
    val error: String? = null
)
