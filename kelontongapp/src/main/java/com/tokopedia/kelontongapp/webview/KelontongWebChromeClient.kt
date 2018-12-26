package com.tokopedia.kelontongapp.webview

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.webkit.GeolocationPermissions
import com.tokopedia.kelontongapp.PERMISSION_REQUEST_LOCATION
import com.tokopedia.kelontongapp.R

class KelontongWebChromeClient(private var activity: Activity?, filePickerInterface: FilePickerInterface) : WebChromeClient() {

    private var callbackBeforeL: ValueCallback<Uri>? = null
    private var callbackAfterL: ValueCallback<Array<Uri?>>? = null

    private var mGeolocationRequestOrigin: String? = null
    private var mGeolocationCallback: GeolocationPermissions.Callback? = null

    private var mCM: String? = null

    private var filePickerInterface: FilePickerInterface? = null
    private var webviewListener: WebviewListener? = null

    init {
        if (filePickerInterface is Activity || filePickerInterface is Fragment) {
            this.filePickerInterface = filePickerInterface
        } else {
            throw RuntimeException("Should be instance of Activity or Fragment")
        }
    }

    fun setWebviewListener(webviewListener: WebviewListener) {
        this.webviewListener = webviewListener
    }


    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        if (activity == null)
            return

        mGeolocationRequestOrigin = null
        mGeolocationCallback = null

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(activity!!)
                        .setMessage(R.string.permission_location_rationale)
                        .setNeutralButton(android.R.string.ok) { _, _ ->
                            mGeolocationCallback = callback
                            mGeolocationRequestOrigin = origin
                            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
                        }
                        .show()
            } else {
                mGeolocationRequestOrigin = origin
                mGeolocationCallback = callback
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
            }
        } else{
            callback!!.invoke(origin, true, true)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mGeolocationCallback != null) {
                mGeolocationCallback!!.invoke(mGeolocationRequestOrigin, true, true)
            }
        } else {
            if (mGeolocationCallback != null) {
                mGeolocationCallback!!.invoke(mGeolocationRequestOrigin, false, false)
            }
        }
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        if (newProgress == PROGRESS_COMPLETED) {
            view.visibility = View.VISIBLE
            if (webviewListener != null)
                webviewListener!!.onComplete()
        }
        super.onProgressChanged(view, newProgress)
    }

    override fun onShowFileChooser(
            webView: WebView, filePathCallback: ValueCallback<Array<Uri?>>,
            fileChooserParams: WebChromeClient.FileChooserParams): Boolean {
        if (callbackAfterL != null) {
            callbackAfterL!!.onReceiveValue(null)
        }
        callbackAfterL = filePathCallback

        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(activity!!.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", mCM)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                mCM = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            } else {
                takePictureIntent = null
            }
        }
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "*/*"

        val intentArray: Array<Intent?>
        if (takePictureIntent != null) {
            intentArray = arrayOf(takePictureIntent)
        } else {
            intentArray = arrayOfNulls(0)
        }

        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        filePickerInterface!!.startActivityForResult(chooserIntent, ATTACH_FILE_REQUEST)
        return true
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= 21) {
            var results: Array<Uri?>? = null
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == callbackAfterL) {
                        return
                    }

                    if (intent == null || intent.data == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = arrayOf(Uri.parse(mCM))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        } else {
                            if (intent.clipData != null) {
                                val numSelectedFiles = intent.clipData!!.itemCount
                                results = arrayOfNulls(numSelectedFiles)
                                for (i in 0 until numSelectedFiles) {
                                    results[i] = intent.clipData!!.getItemAt(i).uri
                                }
                            }
                        }
                    }
                }
            }
            if (callbackAfterL != null) callbackAfterL!!.onReceiveValue(results)
            callbackAfterL = null
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == callbackBeforeL) return
                val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
                callbackBeforeL!!.onReceiveValue(result)
                callbackBeforeL = null
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    interface WebviewListener {
        fun onComplete()
    }

    companion object {

        private val PROGRESS_COMPLETED = 100
        val ATTACH_FILE_REQUEST = 1
    }
}
