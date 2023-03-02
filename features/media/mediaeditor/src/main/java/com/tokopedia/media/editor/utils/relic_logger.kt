package com.tokopedia.media.editor.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

private const val NEW_RELIC_TAG = "MEDIA_EDITOR"

const val FILE_NOT_FOUND_FIELD = "FileNotFound"
const val STORAGE_FULL_FIELD = "StorageFull"
const val OTHER_FAILED_SAVE_FIELD = "FailedSave"
const val MEMORY_LIMIT_FIELD = "MemoryLimit"
const val LOAD_IMAGE_FAILED = "LoadImageFail"

fun newRelicLog(map: Map<String, String>) {
    ServerLogger.log(
        Priority.P2,
        NEW_RELIC_TAG,
        map
    )
}
