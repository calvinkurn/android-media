package com.tokopedia.sellerfeedback.presentation.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object SellerFeedbackLogger {

    const val LOG_TAG = "SELLER_FEEDBACK_KMP"
    const val RESULT_KEY = "result"
    const val STATE_KEY = "state"
    const val SUCCESS = "success"
    const val FAILED = "failed"
    const val DETAIL_RESULT = "detail_result"

    object STATE {
        const val OPEN = "OPEN"
        const val SUBMIT = "SUBMIT"
        const val RESULT = "RESULT"
    }
    fun sendLogToNewRelic(state: String, result: String = "", detailResult: String = "") {
        ServerLogger.log(
            Priority.P2, tag = LOG_TAG,
            mapOf(
                STATE_KEY to state,
                RESULT_KEY to result,
                DETAIL_RESULT to detailResult
            )
        )
    }
}
