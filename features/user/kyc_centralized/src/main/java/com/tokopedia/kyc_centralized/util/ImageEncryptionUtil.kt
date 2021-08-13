package com.tokopedia.kyc_centralized.util

import android.content.Context
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel.Companion.KYC_USING_ENCRYPT
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object ImageEncryptionUtil {
    private const val TEMP_IMAGE_TAG = "temp_"
    private const val SALT = "A%D*G-KaPdRgUkXp2s5v8y/B?E(H+MbQ"
    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val IV_SIZE = 128

    private fun getKey(): SecretKey? {
        var secretKey: SecretKey? = null
        try {
            secretKey = SecretKeySpec(SALT.toBytes(), ALGORITHM)
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
        } catch (ex: Exception) {
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

    fun initAesEncrypt(): Cipher {
        val aes = Cipher.getInstance(ALGORITHM)
        aes.init(Cipher.ENCRYPT_MODE, getKey())
        return aes
    }

    fun initAesDecrypt(tempIv: ByteArray?): Cipher {
        val aes = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(IV_SIZE, tempIv)
        aes.init(Cipher.DECRYPT_MODE, getKey(), spec)

        return aes
    }

    fun writeEncryptedImage(originalFilePath: String, encryptedImagePath: String, aes: Cipher) {
        val fis = FileInputStream(originalFilePath)
        val fs = FileOutputStream(File(encryptedImagePath))
        val out = CipherOutputStream(fs, aes)
        out.write(fis.readBytes(1024 * 1024))
        out.flush()
        out.close()
    }

    fun writeDecryptedImage(originalFilePath: String, decryptedFilePath: String, aes: Cipher) {
        val fis = FileInputStream(originalFilePath)
        val out = CipherInputStream(fis, aes)
        File(decryptedFilePath).outputStream().use {
            out.copyTo(it)
        }
    }

    fun isUsingEncrypt(context: Context) : Boolean {
        try {
            return FirebaseRemoteConfigImpl(context).getBoolean(KYC_USING_ENCRYPT, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
}