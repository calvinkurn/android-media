package com.tokopedia.media.editor.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

private const val NEW_RELIC_TAG = "MEDIA_EDITOR"

const val FAILED_SAVE_FIELD = "FailedSaveGallery"
const val MEMORY_LIMIT_FIELD = "MemoryLimit"
const val LOAD_IMAGE_FAILED = "LoadImageFail"
const val FAILED_CREATE_BITMAP = "FailedCreateBitmap"
const val GENERAL_ERROR = "GeneralError"

fun newRelicLog(map: Map<String, String>) {
    ServerLogger.log(
        Priority.P2,
        NEW_RELIC_TAG,
        map
    )
}
