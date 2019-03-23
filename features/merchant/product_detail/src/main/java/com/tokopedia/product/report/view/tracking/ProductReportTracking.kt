package com.tokopedia.product.report.view.tracking

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker

class ProductReportTracking(private val analyticTracker: AnalyticTracker?) {

    companion object {
        const val REPORT_SUCCESS_CAMEL = "reportSuccess"
        const val REPORT_SUCCESS = "Report Success"
        const val PRODUCT_DETAIL = "Product Detail Page"
    }

    fun eventSubmitReport() {
        analyticTracker?.sendEventTracking(
                REPORT_SUCCESS_CAMEL,
                PRODUCT_DETAIL,
                REPORT_SUCCESS,
                REPORT_SUCCESS)
    }

}
