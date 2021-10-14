package com.tokopedia.imagepicker_insta.util

import androidx.annotation.IntDef
import com.tokopedia.imagepicker_insta.util.WriteStorageLocation.Companion.EXTERNAL
import com.tokopedia.imagepicker_insta.util.WriteStorageLocation.Companion.INTERNAL

object StorageUtil {
    const val WRITE_LOCATION = WriteStorageLocation.INTERNAL
    const val INTERNAL_FOLDER_NAME = "image_picker"
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(INTERNAL, EXTERNAL)
annotation class WriteStorageLocation {
    companion object {
        const val INTERNAL = 0
        const val EXTERNAL = 1
    }
}