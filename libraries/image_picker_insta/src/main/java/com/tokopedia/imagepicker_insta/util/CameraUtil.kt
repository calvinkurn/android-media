package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import com.tokopedia.imagepicker_insta.common.BundleData
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CameraUtil {

    fun getMediaCountText(mediaCount: Int): String {
        return "$mediaCount media"
    }

    fun openCamera(fragment: Fragment, applinkToNavigateAfterMediaCapture: String?, videoMaxDuration:Long) {
        if(fragment.context!=null){
            fragment.startActivity(
                CameraActivity.getIntent(fragment.requireContext(),
                    applinkToNavigateAfterMediaCapture, videoMaxDuration))
        }
    }

    @Throws(IOException::class)
    fun createMediaFile(context: Context, isImage: Boolean = true, storeInCache: Boolean = false): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var storageDir: File? = null

        if (storeInCache) {
            storageDir = context.cacheDir
        } else {
            when (StorageUtil.WRITE_LOCATION) {
                WriteStorageLocation.INTERNAL -> {
                    storageDir = getInternalDir(context)
                }
                WriteStorageLocation.EXTERNAL -> {
                    storageDir = getExternalDir(context, isImage)
                }
            }
        }

        if (isImage) {
            val prefix = "IMG"
            return File.createTempFile(
                "${prefix}_${timeStamp}_",
                ".jpg",
                storageDir
            )
        } else {
            val prefix = "VID"
            return File.createTempFile(
                "${prefix}_${timeStamp}_",
                ".mp4",
                storageDir
            )
        }
    }

    private fun getExternalDir(context: Context, isImage: Boolean = true): File? {
        if (isImage)
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    }

    fun getInternalDir(context: Context): File {
        val file = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        file.mkdirs()
        return file
    }


    fun getIntentfromFileUris(fileUriList: ArrayList<Uri>): Intent {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(BundleData.URIS, fileUriList)
        intent.putExtras(bundle)
        return intent
    }

    fun createApplinkToSendFileUris(applink: String, fileUriList: List<Uri>): String {
        if (!applink.isNullOrEmpty()) {
            val fileUriStringBuilder = StringBuilder()
            fileUriList.forEach {
                fileUriStringBuilder.append(it.toString())
                fileUriStringBuilder.append(",")
            }
            if (fileUriStringBuilder.isNotEmpty()) {
                fileUriStringBuilder.removeSuffix(",")
            }
            val finalApplink = Uri.parse(applink)
                .buildUpon()
                .appendQueryParameter(BundleData.URIS, fileUriStringBuilder.toString())
                .build()
                .toString()
            return finalApplink
        }
        return ""
    }
}