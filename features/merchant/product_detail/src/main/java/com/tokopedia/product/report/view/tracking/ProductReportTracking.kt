package com.tokopedia.product.report.view.tracking

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

class ProductReportTracking() {

    companion object {
        const val REPORT_SUCCESS_CAMEL = "reportSuccess"
        const val REPORT_SUCCESS = "Report Success"
        const val PRODUCT_DETAIL = "Product Detail Page"
    }

    fun eventSubmitReport() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                REPORT_SUCCESS_CAMEL,
                PRODUCT_DETAIL,
                REPORT_SUCCESS,
                REPORT_SUCCESS))
    }

}
