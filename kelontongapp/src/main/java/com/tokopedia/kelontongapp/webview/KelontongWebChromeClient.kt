package com.tokopedia.kelontongapp.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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

class KelontongWebChromeClient(context: Context, filePickerInterface: FilePickerInterface) : WebChromeClient() {

    private var callbackBeforeL: ValueCallback<Uri>? = null
    private var callbackAfterL: ValueCallback<Array<Uri?>>? = null

    private var mCM: String? = null

    private var context: Context? = null

    private var filePickerInterface: FilePickerInterface? = null
    private var webviewListener: WebviewListener? = null

    init {
        if (filePickerInterface is Activity || filePickerInterface is Fragment) {
            this.context = context
            this.filePickerInterface = filePickerInterface
        } else {
            throw RuntimeException("Should be instance of Activity or Fragment")
        }
    }

    fun setWebviewListener(webviewListener: WebviewListener) {
        this.webviewListener = webviewListener
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
        callbackAfterL = filePathCallback

        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(context!!.packageManager) != null) {
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
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (intent == null)
            return
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
