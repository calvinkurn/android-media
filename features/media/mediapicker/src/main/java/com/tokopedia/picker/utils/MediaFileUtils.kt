package com.tokopedia.picker.utils

import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File

object MediaFileUtils {

    fun createMediaFile(isImage: Boolean = true): File {
        val uniqueFileName = FileUtil.generateUniqueFileName()
        val currentTimeInMillis = System.currentTimeMillis()

        val internalAppCacheDir = FileUtil
            .getTokopediaInternalDirectory(ImageProcessingUtil.DEFAULT_DIRECTORY)
            .absolutePath

        return File(
            internalAppCacheDir,
            if (isImage) {
                "IMG_${uniqueFileName}_${currentTimeInMillis}.jpg"
            } else {
                "VID_${uniqueFileName}_${currentTimeInMillis}.mp4"
            }
        )
    }

}