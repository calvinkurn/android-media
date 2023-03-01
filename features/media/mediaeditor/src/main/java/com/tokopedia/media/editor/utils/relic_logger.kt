package com.tokopedia.media.editor.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

private const val NEW_RELIC_TAG = "MEDIA_EDITOR_FILE_NOT_FOUND"

fun relicLogFileNotFound(map: Map<String, String>) {
    newRelicLog(
        key = NEW_RELIC_TAG,
        map = map
    )
}

private fun newRelicLog(map: Map<String, String>, key: String) {
    ServerLogger.log(
        Priority.P2,
        key,
        map
    )
}
