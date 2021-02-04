package com.tokopedia.kyc_centralized.util

import java.io.File
import java.io.IOException
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object ImageEncryptionUtil {
    private const val TEMP_IMAGE_TAG = "temp_"
    private var salt = "r5u8x/A?D(G+KbPe"
    const val ALGORITHM = "AES"

    fun getKey(): SecretKey? {
        var secretKey: SecretKey? = null
        try {
            secretKey = SecretKeySpec(salt.toBytes(), ALGORITHM)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return secretKey
    }

    private fun String.toBytes(): ByteArray {
        return this.toByteArray(Charsets.UTF_8)
    }

    fun createCopyOfOriginalFile(originalFilePath: String): String {
        val filePath = getImageParentPath(originalFilePath)
        val imageName = getImageNameFromPath(originalFilePath)

        val originalFile = File(originalFilePath)
        val copyFile = File(filePath, "$TEMP_IMAGE_TAG$imageName")

        //Create a copy of original file
        try {
            originalFile.copyTo(copyFile)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return copyFile.path
    }

    private fun getImageParentPath(path: String?): String {
        var newPath = ""
        path?.let {
            newPath = it.substring(0, it.lastIndexOf("/") + 1)
        }
        return newPath
    }

    private fun getImageNameFromPath(path: String?): String {
        var newPath = ""
        path?.let {
            newPath = it.substring(it.lastIndexOf("/") + 1)
        }
        return newPath
    }

    fun deleteFile(path: String) {
        val file = File(path)

        if (file.exists())
            file.delete()
    }

    fun renameImageToOriginalFileName(path: String): String {
        val filePath = getImageParentPath(path)
        val imageName = getImageNameFromPath(path)

        val from = File(filePath, imageName)

        val renameTo = imageName.replace(TEMP_IMAGE_TAG, "")

        val to = File(filePath, renameTo)
        if (from.exists())
            from.renameTo(to)

        return to.path
    }
}