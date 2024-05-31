package com.tokopedia.creation.common.upload.util

import android.content.ContentResolver
import android.net.Uri

/**
 * Created by Jonathan Darwin on 19 April 2024
 */

internal fun getFileAbsolutePath(path: String): String? {
    return if (path.startsWith("${ContentResolver.SCHEME_FILE}://")) {
        Uri.parse(path).path
    } else {
        path
    }
}
