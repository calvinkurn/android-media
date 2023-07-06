package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.util.isVideoFormat
import com.tokopedia.picker.common.utils.fileExtension
import io.mockk.every
import io.mockk.mockk
import java.io.File

fun generateMockVideoFile(
    length: Long = 0
) = generateMockFile(
    isVideo = true,
    isExist = true,
    length = length
)

fun mockNotExistVideoFile() = generateMockFile(isVideo = true, isExist = false)
fun generateMockImageFile() = generateMockFile(isVideo = false, isExist = true)
fun mockNotExistImageFile() = generateMockFile(isVideo = false, isExist = false)

private fun generateMockFile(
    isVideo: Boolean,
    isExist: Boolean = true,
    length: Long = 0
): File {
    val file = mockk<File>(relaxed = true)

    every { file.exists() } returns isExist
    every { file.path } returns if (isVideo) "foo.mp4" else "bar.jpg"
    every { fileExtension(any()) } returns if (isVideo) ".mp4" else ".jpg"
    every { isVideoFormat(any()) } returns isVideo
    every { file.length() } returns length

    return file
}
