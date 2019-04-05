package com.tokopedia.imagepreview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImagePreviewUtils{

    fun getUri(context: Context, outputMediaFile: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context,
                    context.applicationContext.packageName + ".provider", outputMediaFile)
        } else {
            Uri.fromFile(outputMediaFile)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun processPictureName(index: Int): String {
        val date = Date()
        val sdf = SimpleDateFormat("ddMMyyyyhhmmss")
        val dateString = sdf.format(date)
        return dateString.replace("[^a-zA-Z0-9]".toRegex(), "") + "_" + Integer.toString(index + 1)
    }

    fun addImageToGallery(filePath: String, context: Context) {
        try {
            val values = ContentValues()

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.MediaColumns.DATA, filePath)

            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (ex: Exception) {
        }

    }

    fun saveImageFromBitmap(context: Activity, bitmap: Bitmap, PicName: String): String? {
        val pictureFile = getOutputMediaFile(PicName)
        var path = ""
        if (pictureFile == null) {
            return null
        }
        try {
            val fos = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            addImageToGallery(pictureFile.getPath(), context)
            path = pictureFile.getPath()
            fos.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return path
    }

    private fun getOutputMediaFile(PicName: String): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(Environment.getExternalStorageDirectory(), "Tokopedia")

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val mediaFile: File
        val mImageName = "$PicName.jpg"
        mediaFile = File(mediaStorageDir, mImageName)
        return mediaFile
    }
}