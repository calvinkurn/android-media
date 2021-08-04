package com.tokopedia.imagepicker_insta.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

object CameraUtil {

    val REQUEST_IMAGE_CAPTURE = 200
    fun openCamera(weakFragment: WeakReference<Fragment?>?):String?{
        return dispatchTakePictureIntent(weakFragment)
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun dispatchTakePictureIntent(weakFragment: WeakReference<Fragment?>?):String? {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            weakFragment?.get()?.context?.let {context->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile(context)
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
                    photoFile?.absolutePath
                }
            }

        }
        return null
    }
}