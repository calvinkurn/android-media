package com.tokopedia.mediauploader

sealed class DebugMediaLoaderEvent {
    object Upload : DebugMediaLoaderEvent()

    data class FileChosen(
        val filePath: String
    ) : DebugMediaLoaderEvent()
}
