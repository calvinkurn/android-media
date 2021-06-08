package com.tokopedia.telephony_masking.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object TelephonyAnalytics {

    private const val EVENT_SAVE = ""
    private const val CATEGORY_SAVE = ""
    private const val ACTION_SAVE = ""
    private const val LABEL_SAVE = ""

    private const val EVENT_CANCEL = ""
    private const val CATEGORY_CANCEL = ""
    private const val ACTION_CANCEL = ""
    private const val LABEL_CANCEL = ""

    fun eventSuccessSaveContact() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_SAVE,
                CATEGORY_SAVE,
                ACTION_SAVE,
                LABEL_SAVE
        ))
    }

    fun eventCancelSaveContact() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CANCEL,
                CATEGORY_CANCEL,
                ACTION_CANCEL,
                LABEL_CANCEL
        ))
    }
}