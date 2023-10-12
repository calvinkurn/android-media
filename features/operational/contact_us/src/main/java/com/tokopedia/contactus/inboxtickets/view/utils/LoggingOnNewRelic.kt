package com.tokopedia.contactus.inboxtickets.view.utils

import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority

object LoggingOnNewRelic {
    private const val NEW_RELIC_LOG_TAG = "CONTACT_US_CLEAR_CACHE"

    fun sendToNewRelicLog() {
        val map = HashMap<String, String>()
        map["action"] = "clear_cache_on_contact_us"
        log(Priority.P2, NEW_RELIC_LOG_TAG, map)
    }
}
