package com.tokopedia.kelontongapp.webview

import android.annotation.TargetApi
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

/**
 * Created by meta on 12/10/18.
 */
class KelontongWebviewClient(private val activity: Activity) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        val uri = Uri.parse(url)
        return handleUri(view, uri)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val uri = request.url
        return handleUri(view, uri)
    }

    private fun handleUri(view: WebView, uri: Uri): Boolean {
        view.loadUrl(uri.toString())
        return true
    }

    fun checkPermission(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(activity, CAMERA)
        val resultWrite = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE)
        return resultCamera == PackageManager.PERMISSION_GRANTED && resultWrite == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (!(writeAccepted && cameraAccepted))
                    Toast.makeText(activity, "Mohon untuk memberikan izin akses kamera dan menulis file.", Toast.LENGTH_SHORT).show()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (activity.shouldShowRequestPermissionRationale(CAMERA)) {
                        showMessageOKCancel("Mohon untuk memberikan izin akses kamera dan menulis file.", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                requestPermission()
                            }
                        })
                        return
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Batal", null)
                .create()
                .show()
    }

    companion object {

        private val PERMISSION_REQUEST_CODE = 2312
    }
}
