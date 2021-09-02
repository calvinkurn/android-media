package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

object CameraUtil {

    const val LOG_TAG = "INSTA_CAM"

    val REQUEST_IMAGE_CAPTURE = 200
    fun openCamera(weakFragment: WeakReference<Fragment?>?): String? {
//        return dispatchTakePictureIntent(weakFragment)
        weakFragment?.get()?.let {
            it.startActivity(CameraActivity.getIntent(it.requireContext()))
        }
        return null

    }

    @Throws(IOException::class)
    fun createMediaFile(context: Context, isImage: Boolean = true): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var storageDir: File? = null

        when (StorageUtil.WRITE_LOCATION) {
            WriteStorageLocation.INTERNAL -> {
                storageDir = getInternalDir(context)
            }
            WriteStorageLocation.EXTERNAL -> {
                storageDir = getExternalDir(context, isImage)
            }
        }
        if (isImage) {
            return File.createTempFile(
                "IMG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
        } else {
            return File.createTempFile(
                "VID_${timeStamp}_", /* prefix */
                ".mp4", /* suffix */
                storageDir /* directory */
            )
        }
    }

    private fun getExternalDir(context: Context, isImage: Boolean = true): File? {
        if(isImage)
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
    }

    private fun getInternalDir(context: Context): File {
        val file = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)
        file.mkdirs()
        return file
    }

    private fun dispatchTakePictureIntent(weakFragment: WeakReference<Fragment?>?): String? {
        var filePath: String? = null
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            weakFragment?.get()?.context?.let { context ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createMediaFile(context)
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        weakFragment?.get()?.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                    filePath = photoFile?.absolutePath
                }
            }

        }
        return filePath
    }
}