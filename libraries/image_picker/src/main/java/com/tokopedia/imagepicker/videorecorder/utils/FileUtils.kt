package com.tokopedia.videorecorder.utils

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.*

/**
 * Created by isfaaghyth on 04/03/19.
 * github: @isfaaghyth
 */
object FileUtils {

    private const val TEMP_FILE     = "tkpd_video.temp"
    private const val VIDEO_EXT     = ".mp4"
    private const val MAIN_DIR      = "Tokopedia/"
    private const val DIR_PREFIX    = "tkpdvideo"
    const val RESULT_DIR            = "$MAIN_DIR/$DIR_PREFIX/"

    /**
     * get Tokopedia's public directory
     * it will be create a new directory if doesn't exist
     * @param: path
     */
    private fun publicDirectory(path: String): File {
        val filePath = "${Environment.getExternalStorageDirectory().absolutePath}/$path/"
        val directory = File(filePath)
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return Environment.getDownloadCacheDirectory()
            }
        }
        return directory
    }

    private fun generateUniqueName(): String {
        val timeString = System.currentTimeMillis().toString()
        val length = timeString.length
        var startIndex = length - 5
        if (startIndex < 0) startIndex = 0
        return timeString.substring(startIndex) + Random().nextInt(100)
    }

    /**
     * check if the file in Tokopedia main directory
     * @param: file
     */
    private fun inTokopediaDir(file: File): Boolean
            = file.exists() && file.absolutePath.contains(MAIN_DIR)

    /**
     * for video generator purpose
     * @param: path
     */
    fun videoPath(path: String): File {
        val mainDir = publicDirectory(path)
        return File(mainDir.absolutePath, "${generateUniqueName()}$VIDEO_EXT")
    }

    fun deleteCacheDir() {
        val mainDir = publicDirectory(RESULT_DIR)
        if (mainDir.exists()) {
            val files = mainDir.listFiles() ?: return
            for (i in files.indices) {
                if (!files[i].isDirectory) {
                    files[i].delete()
                }
            }
            mainDir.delete()
        }
    }

    fun deleteFile(fileToDelete: String) {
        if (fileToDelete.isEmpty()) return
        val file = File(fileToDelete)
        if (inTokopediaDir(file)) {
            file.delete()
        }
    }

    fun deleteFiles(filesToDelete: Array<String>) {
        if (filesToDelete.isEmpty()) return
        for (file: String in filesToDelete) {
            deleteFile(file)
        }
    }

    fun generateTempFile(path: String): File {
        val file = publicDirectory(path)
        return File(file.absolutePath, TEMP_FILE)
    }

    fun moveToDir(sourceFile: File?): File {
        return moveToDir(sourceFile, RESULT_DIR)!!
    }

    fun moveToDir(sourceFile: File?, path: String): File? {
        val file: File
        try {
            file = publicDirectory(path)
            sourceFile?.absolutePath?.let {
                source -> copyFile(source, file.absolutePath)
            }
        } catch (e: Throwable) {
            return null
        }

        return file.exists() then { file } elze { null }
    }

    private fun copyFile(pathFrom: String, pathTo: String) {
        if (pathFrom.equals(pathTo, true)) return

        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(File(pathFrom)).channel
            outputChannel = FileOutputStream(File(pathTo)).channel
            inputChannel.transferTo(0, inputChannel.size(), outputChannel)
            inputChannel.close()
        } finally {
            inputChannel?.close()
            outputChannel?.close()
        }
    }

}