package com.tokopedia.mediauploader.common.util

import com.tokopedia.utils.file.FileUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.math.ceil

private const val VIDEO_PATH = "Tokopedia/"

/**
 * This path generator consumed for generate video sliced path.
 * The parent directory comes from internal_app dir following "Tokopedia/cache/"
 *
 * For example, the parent file (the large one) like this:
 * videoplayback.mp4
 *
 * the generator should be generated like this:
 * dir : {internal_app}/Tokopedia/cache/videoplayback
 * sliced file : {internal_app}/Tokopedia/cache/videoplayback/{counter}_videoplayback.mp4
 */
fun generateAbsolutePathOfVideo(file: File, counter: Int): String {
    // get parent file name without extension
    val videoNameWithoutExtension = file.name.substring(0, file.name.lastIndexOf("."))

    // set destination directory to save
    val dirResult = FileUtil.getTokopediaInternalDirectory(VIDEO_PATH)
    val dirSplitFileResult = File("$dirResult/$videoNameWithoutExtension")

    // create directory if not exist
    if (!dirSplitFileResult.exists()) {
        dirSplitFileResult.mkdirs()
    }

    return "${dirSplitFileResult.absolutePath}/${String.format("%02d", counter)}_${file.name}"
}

fun File.slice(sizeInMb: Int = 10): List<File> {
    if (!this.exists()) return emptyList()

    // store file sliced result
    val slicedVideoFiles = mutableListOf<File>()

    // set the suffix counter of video file name
    var counterOfSuffixFileName = 1

    /*
    * this is for the name of video sliced result:
    * for example, the original file name is "videoplayback"
    * the result it should be like this "01_videoplayback.mp4"
    * */
    val firstSlicedVideoFile = generateAbsolutePathOfVideo(this, counterOfSuffixFileName)

    // read the original video file
    val inputStream = FileInputStream(this)

    // creations for sliced video file
    var outputStream = FileOutputStream(firstSlicedVideoFile)

    // add file to temp list
    slicedVideoFiles.add(File(firstSlicedVideoFile))

    // convert to bytes
    val sizeOfChunks = (1024 * 1024 * sizeInMb).toDouble()

    // get chunks size from sizeOfChunks
    val chunks = ceil(this.length() / sizeOfChunks).toInt()

    // set split size of every single file
    val splitSize = inputStream.available() / chunks

    var streamSize = 0
    var byteRead: Int

    do {
        byteRead = inputStream.read()

        if (splitSize == streamSize) {
            if (counterOfSuffixFileName != chunks) {
                counterOfSuffixFileName++

                val slicedVideoFile = generateAbsolutePathOfVideo(this, counterOfSuffixFileName)

                outputStream = FileOutputStream(slicedVideoFile)

                // add file to temp list
                slicedVideoFiles.add(File(slicedVideoFile))

                streamSize = 0
            }
        }

        outputStream.write(byteRead)
        streamSize++
    } while (byteRead != -1)

    return slicedVideoFiles
}

fun File.slice(
    partNumber: Int,
    chunkSize: Int,
): ByteArray? {
    if (partNumber < 0) return null
    if (chunkSize < 0) return null

    val inputStream = FileInputStream(this)
    val byteArray = ByteArray(chunkSize)

    val offset = (partNumber - 1) * chunkSize
    inputStream.skip(offset.toLong())

    inputStream.read(byteArray, 0, chunkSize)

    return byteArray
}

fun ByteArray.trimLastZero(): ByteArray {
    var length = this.size - 1

    for (i in length downTo 0) {
        if (this[i].toInt() == 0) {
            length--
        } else {
            break
        }
    }

    return this.copyOf(length + 1)
}