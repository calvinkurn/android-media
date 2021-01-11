package com.tokopedia.pms.howtopay_native.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.io.File
import java.io.FileOutputStream

class ScreenshotHelper(private val onSuccess: () -> Unit) : PermissionCheckerHelper.PermissionCheckListener {

    private var view: View? = null
    private val permissionCheckerHelper = PermissionCheckerHelper()
    private val filePrefix = "store_"
    private val fileExt = ".jpg"


    fun takeScreenShot(view: View?, fragment: Fragment) {
        this.view = view;
        permissionCheckerHelper.checkPermission(fragment,
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                this)
    }

    private fun getScreenShotBitmap(): Bitmap? {
        view?.let { view ->
            val b = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            view.draw(c)
            return b
        }
        return null
    }

    fun onRequestPermissionsResult(context: Context?, requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(context,
                        requestCode, permissions, grantResults)
            }
        }
    }

    private fun saveToExtDirectory(bitmap: Bitmap?) {
        bitmap?.let {
            val context = view?.context?.applicationContext
            context?.let { context ->
                val filename = "$filePrefix${System.currentTimeMillis()}$fileExt"
                val root = Environment.getExternalStorageDirectory().toString()
                val sd = File("$root/${Environment.DIRECTORY_PICTURES}")
                if (!sd.exists())
                    sd.mkdir()
                val dest = File(sd, filename)
                try {
                    val out = FileOutputStream(dest)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                    out.flush()
                    out.close()
                    MediaScannerConnection.scanFile(context, arrayOf(dest.toString()), null, null)
                    onSuccess.invoke()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            bitmap.recycle()
        }
    }

    override fun onPermissionDenied(permissionText: String) {
    }

    override fun onNeverAskAgain(permissionText: String) {
    }

    override fun onPermissionGranted() {
        saveToExtDirectory(getScreenShotBitmap())
    }

}
