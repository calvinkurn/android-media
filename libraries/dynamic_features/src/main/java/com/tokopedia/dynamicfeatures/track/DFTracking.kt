package com.tokopedia.dynamicfeatures.track

import com.tokopedia.track.TrackApp

/**
 * Created by hendry on 13/12/19.
 */
class DFTracking {
    companion object {
        const val EVENT = "/df download pages"
        const val CAT_DL_BG = "download background"
        const val CAT_DL = "download page"
        const val ACTION_SUCCESS = "success"
        const val ACTION_FAILED = "failed"

        fun trackDownloadDF(moduleNameList: List<String>,
                            errorCodeList: List<String>? = null,
                            isInBackground: Boolean = false) {
            val errorCode = if (errorCodeList == null || errorCodeList.isEmpty()) {
                ""
            } else {
                errorCodeList.last()
            }
            val isSuccess = errorCode.isEmpty()
            moduleNameList.forEach { moduleName ->
                TrackApp.getInstance().gtm.sendGeneralEvent(
                    EVENT,
                    if (isInBackground) {
                        CAT_DL_BG
                    } else {
                        CAT_DL
                    },
                    if (isSuccess) {
                        ACTION_SUCCESS
                    } else {
                        ACTION_FAILED
                    },
                    moduleName + if (isSuccess) {
                        ""
                    } else {
                        " - $errorCode"
                    }
                )
            }
        }
    }
}