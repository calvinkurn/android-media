package com.tokopedia.report.data.util

import com.tokopedia.track.TrackApp

class MerchantReportTracking {

    fun eventReportReason(reasonLabel: String, label: String = ""){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT,
                CATEGORY, String.format(ACTION_FORMAT, reasonLabel), label)
    }

    fun eventReportLearnMore(reason: String? = null){
        eventReportReason(if (reason == null) ACTION_LEARN_MORE else "$ACTION_LEARN_MORE di details - $reason")
    }

    fun eventReportClickDetail(reason: String){
        eventReportReason(String.format(ACTION_REPORT_CLICK_DETAIL, reason))
    }

    fun eventReportAddPhotoOK(reason: String) {
        eventReportReason(String.format(ACTION_REPORT_ADD_PHOTO, reason), "success")
    }

    fun eventReportAddPhotoFail(reason: String) {
        eventReportReason(String.format(ACTION_REPORT_ADD_PHOTO, reason), "failed")
    }

    fun eventReportClickLink(label: String, reason: String) {
        eventReportReason(String.format(ACTION_FORMAT, "$label - $reason"))
    }

    fun eventReportCancelDisclaimer(reason: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT,
                String.format(CATEGORY_DISCLAIMER, reason), ACTION_CANCEL_DISCLAIMER, "")
    }

    fun eventReportLaporDisclaimer(reason: String, isSuccess: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(EVENT,
                String.format(CATEGORY_DISCLAIMER, reason), ACTION_LAPOR_DISCLAIMER,
                if (isSuccess)"success" else "failed")
    }

    companion object{
        private const val EVENT = "clickReport"
        private const val CATEGORY = "report product"
        private const val CATEGORY_DISCLAIMER = "report disclaimer - %s"
        private const val ACTION_FORMAT = "click %s"
        private const val ACTION_LEARN_MORE = "pelajari lebih lanjut"
        private const val ACTION_REPORT_CLICK_DETAIL = "lapor di details - %s"
        private const val ACTION_REPORT_ADD_PHOTO = "tambah foto di details - %s"
        private const val ACTION_CANCEL_DISCLAIMER = "click batal di disclaimer"
        private const val ACTION_LAPOR_DISCLAIMER = "click lapor di disclaimer"
    }
}