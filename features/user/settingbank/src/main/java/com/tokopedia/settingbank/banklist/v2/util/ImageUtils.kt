package com.tokopedia.settingbank.banklist.v2.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File

object ImageUtils {


    const val EXT_JPG = "jpg"
    const val EXT_JPEG = "jpeg"
    const val EXT_PNG = "png"

    const val IMAGE_QUALITY = 95

    fun getFileName(filePath: String): String {
        val file = File(filePath)
        return file.name
    }

    fun getFileExt(filePath: String): String {
        return when {
            filePath.endsWith(EXT_JPEG, true) -> EXT_JPEG
            filePath.endsWith(EXT_JPG, true) -> EXT_JPG
            else -> EXT_PNG
        }
    }

    fun getMimeType(context: Context, file: File): String? {
        val uri = Uri.fromFile(file)
        var mimeType: String? = null
        mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = context?.contentResolver
            cr?.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
        }
        return mimeType
    }

    fun encodeToBase64(imagePath: String): String {
        val bm = BitmapFactory.decodeFile(imagePath)
        val baos = ByteArrayOutputStream()
        val format = when {
            imagePath.endsWith(EXT_JPEG, true) -> Bitmap.CompressFormat.JPEG
            imagePath.endsWith(EXT_JPG, true) -> Bitmap.CompressFormat.JPEG
            else -> Bitmap.CompressFormat.PNG
        }
        bm.compress(format, IMAGE_QUALITY, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}