package com.tokopedia.editor.util

import com.tokopedia.utils.file.FileUtil

private const val CACHE_FOLDER = "Tokopedia/editor-stories"

fun getEditorCacheFolderPath(): String {
    return FileUtil.getTokopediaInternalDirectory(CACHE_FOLDER).path + "/"
}
