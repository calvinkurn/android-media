package com.tokopedia.report.view.fragment.models

import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 12/09/22"
 * Project name: android-tokopedia-core
 **/

sealed class ProductReportUiEvent {
    object LoadData : ProductReportUiEvent()
    data class OnItemClicked(val reason: ProductReportReason) : ProductReportUiEvent()
    data class OnFooterClicked(val url: String) : ProductReportUiEvent()

    data class OnScrollTop(val reason: ProductReportReason) : ProductReportUiEvent()
    data class OnGoToForm(val reason: ProductReportReason) : ProductReportUiEvent()
    data class OnToasterError(val error: Throwable) : ProductReportUiEvent()
    object OnBackPressed : ProductReportUiEvent()
}
