package com.tokopedia.home.account.presentation.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment
import java.util.regex.Pattern

class WebViewFragment : BaseWebViewFragment() {

    private var userSession: UserSessionInterface? = null
    private var isTokopediaUrl: Boolean = false
    val TOKOPEDIA_STRING = "tokopedia"
    private var url: String? = null
    private val Insurance_File_Name = "E-policy Asuransi"

    private val pdf_regex_pattern = "^.+\\.([pP][dD][fF])\$"


    companion object {
        val ARGS_URL = "KEY_URL"

        fun newInstance(url: String): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            args.putString(ARGS_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments!!.getString("KEY_URL")
        isTokopediaUrl = Uri.parse(url).host!!.contains(TOKOPEDIA_STRING)
        userSession = UserSession(activity!!.applicationContext)
    }

    protected fun getPlainUrl(): String? {
        return url
    }


    override fun getUrl(): String? {
        if (isTokopediaUrl) {
            val gcmId = userSession?.getDeviceId()
            val userId = userSession?.getUserId()
            return URLGenerator.generateURLSessionLogin(
                    Uri.encode(getPlainUrl()),
                    gcmId,
                    userId)
        } else {
            return url
        }
    }

    override fun getUserIdForHeader(): String? {
        return if (isTokopediaUrl) {
            userSession?.getUserId()
        } else null
    }

    override fun getAccessToken(): String? {
        return if (isTokopediaUrl) {
            userSession?.getAccessToken()
        } else null
    }


    @SuppressLint("MissingPermission")
    override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
        if (isdownloadable(url)) {
            val downloadHelper = DownloadHelper(activity!!, url, Insurance_File_Name, object : DownloadHelper.DownloadHelperListener {
                override fun onDownloadComplete() {
                }
            })

            downloadHelper.downloadFile(::isdownloadable)

            return true
        } else if (RouteManager.isSupportApplink(activity, url)) {
            RouteManager.route(activity, url)
            return true
        }
        return false
    }

    private fun isdownloadable(uri: String): Boolean {
        val pattern = Pattern.compile(pdf_regex_pattern)
        val matcher = pattern.matcher(uri)
        return matcher.find()
    }


}