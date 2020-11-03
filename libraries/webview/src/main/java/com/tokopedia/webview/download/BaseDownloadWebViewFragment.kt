package com.tokopedia.webview.download

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import com.google.gson.Gson
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class BaseDownloadWebViewFragment : BaseSessionWebViewFragment() {

    private var extArray: Array<String>? = null
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper

    companion object {
        val ARGS_EXT = "KEY_EXT"

        fun newInstance(url: String, extensions: String): BaseDownloadWebViewFragment {
            val bundle = Bundle()
            bundle.putString(KEY_URL,url)
            bundle.putString(ARGS_EXT, extensions)
            val thisFragment = BaseDownloadWebViewFragment()
            thisFragment.arguments = bundle
            return thisFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val converter = Gson()
        extArray= converter.fromJson<Array<String>>(arguments!!.getString(ARGS_EXT),Array<String>::class.java)
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
        if (isdownloadable(url)) {
            checkPermissionAndDownload(url)
            return true
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }

    private fun checkPermissionAndDownload(url: String) {
        permissionCheckerHelper = PermissionCheckerHelper()
        permissionCheckerHelper.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
            }

            override fun onNeverAskAgain(permissionText: String) {
            }

            override fun onPermissionGranted() {
                downloadFile(url)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun downloadFile(url: String) {
        val downloadHelper = DownloadHelper(activity!!, url, fetchFileName(url), object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
            }
        })

        downloadHelper.downloadFile(::isdownloadable)
    }

    private fun isdownloadable(uri: String): Boolean {
        if (!extArray.isNullOrEmpty()) {
            for (i in extArray!!) {
                if ((uri.toLowerCase()).endsWith(i)) {
                    return true
                }
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(activity!!.applicationContext, requestCode, permissions, grantResults)
        }
    }

    private fun fetchFileName(uri: String): String {
        return uri.substring(uri.lastIndexOf("/") + 1)
    }
}