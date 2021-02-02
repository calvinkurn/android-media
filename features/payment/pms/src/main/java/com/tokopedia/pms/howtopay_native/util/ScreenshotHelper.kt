package com.tokopedia.pms.howtopay_native.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ScreenshotHelper(private val onSuccess: () -> Unit) : PermissionCheckerHelper.PermissionCheckListener, CoroutineScope {

    private var view: View? = null
    private val permissionCheckerHelper = PermissionCheckerHelper()
    private val filePrefix = "store_"
    private val fileExt = ".jpg"

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun takeScreenShot(view: View?, fragment: Fragment) {
        this.view = view;
        permissionCheckerHelper.checkPermission(fragment,
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                this)
    }

    private fun getScreenShotBitmap(onBitmap: (Bitmap?) -> Unit) {
        view?.let { view ->
            view.post {
                val b = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val c = Canvas(b)
                view.draw(c)
                onBitmap(b)
            }
        }
    }

    fun onRequestPermissionsResult(context: Context?, requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(context,
                        requestCode, permissions, grantResults)
            }
        }
    }

    override fun onPermissionDenied(permissionText: String) {
    }

    override fun onNeverAskAgain(permissionText: String) {
    }

    override fun onPermissionGranted() {
        getScreenShotBitmap(::saveToExtDirectory)
    }

    private fun saveToExtDirectory(bitmap: Bitmap?) {
        bitmap?.let {
            val applicationContext = view?.context?.applicationContext
            launchCatchError(block = {
                val dest = getDestinationFile()
                writeBitmapToFile(bitmap, dest)
                applicationContext?.let {
                    MediaScannerConnection.scanFile(applicationContext,
                            arrayOf(dest.toString()), null, null)
                }
                onSuccess.invoke()
                bitmap.recycle()
            }, onError = {
                it.stackTrace
            })

        }
    }

    private suspend fun writeBitmapToFile(bitmap: Bitmap, file: File) = withContext(Dispatchers.IO) {
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            out.flush()
            out.close()
        } catch (e: IOException) {
        }
    }

    private suspend fun getDestinationFile(): File = withContext(Dispatchers.IO) {
        val filename = "$filePrefix${System.currentTimeMillis()}$fileExt"
        val root = Environment.getExternalStorageDirectory().toString()
        val sd = File("$root/${Environment.DIRECTORY_PICTURES}")
        if (!sd.exists())
            sd.mkdir()
        return@withContext File(sd, filename)
    }

    fun cancel() {
        if (job.isActive)
            job.cancel()
    }

}
