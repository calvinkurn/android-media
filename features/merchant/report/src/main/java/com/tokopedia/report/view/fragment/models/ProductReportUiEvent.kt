package com.tokopedia.report.view.fragment.models

import com.tokopedia.report.data.model.ProductReportReason

/**
 * Created by yovi.putra on 12/09/22"
 * Project name: android-tokopedia-core
 **/

sealed class ProductReportUiEvent {
    data class ItemClicked(val reason: ProductReportReason) : ProductReportUiEvent()
    data class FooterClicked(val url: String) : ProductReportUiEvent()
}
