package com.tokopedia.utils.video

import com.tokopedia.utils.file.FileUtil
import java.io.File

object VideoUtil {
    const val VIDEO_DIR = "video/"
    const val VIDEO_EXT = ".mp4"

    fun getTokopediaVideoPath(relativePathDirectory: String? = VIDEO_DIR): File {
        return File(FileUtil.getTokopediaInternalDirectory(relativePathDirectory).absolutePath,
                FileUtil.generateUniqueFileName() + VIDEO_EXT)
    }
}