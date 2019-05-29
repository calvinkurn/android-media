package com.tokopedia.webview.download

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.google.gson.Gson
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment

class BaseDownloadWebViewFragment : BaseWebViewFragment() {

    private var userSession: UserSessionInterface? = null
    private var isTokopediaUrl: Boolean = false
    val TOKOPEDIA_STRING = "tokopedia"
    private var url: String? = null
    private var extArray: Array<String>? = null
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper


    companion object {
        val ARGS_URL = "KEY_URL"
        val ARGS_EXT = "KEY_EXT"

        fun newInstance(url: String, extensions: String): BaseDownloadWebViewFragment {
            val fragment = BaseDownloadWebViewFragment()
            val args = Bundle()
            args.putString(ARGS_URL, url)
            args.putString(ARGS_EXT, extensions)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments!!.getString("KEY_URL")
        val converter = Gson()
        extArray= converter.fromJson<Array<String>>(arguments!!.getString("KEY_EXT"),Array<String>::class.java)
        isTokopediaUrl = Uri.parse(url).host!!.contains(TOKOPEDIA_STRING)
        userSession = UserSession(activity!!.applicationContext)
    }

    protected fun getPlainUrl(): String? {
        return url
    }


    override fun getUrl(): String? {
        return if (isTokopediaUrl) {
            val gcmId = userSession?.deviceId
            val userId = userSession?.userId
            URLGenerator.generateURLSessionLogin(
                    Uri.encode(getPlainUrl()),
                    gcmId,
                    userId)
        } else {
            url
        }
    }

    override fun getUserIdForHeader(): String? {
        return if (isTokopediaUrl) {
            userSession?.getUserId()
        } else null
    }

    override fun getAccessToken(): String? {
        return if (isTokopediaUrl) {
            userSession?.accessToken
        } else null
    }


    override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
        if (isdownloadable(url)) {

            checkPermissionAndDownload(url)

            return true
        } else if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, url)
            return true
        }
        return false
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