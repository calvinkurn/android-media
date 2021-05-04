package com.tokopedia.webview.download

import android.os.Bundle
import android.webkit.WebView
import com.google.gson.Gson
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_URL

class BaseDownloadWebViewFragment : BaseSessionWebViewFragment() {

    private var extArray: Array<String>? = null

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
        extArray= converter.fromJson(requireArguments().getString(ARGS_EXT),Array<String>::class.java)
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
        if (isdownloadable(url)) {
            downloadFile(url)
            return true
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }

    private fun downloadFile(url: String) {
        val downloadHelper = DownloadHelper(requireActivity(), url, fetchFileName(url), object : DownloadHelper.DownloadHelperListener {
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

    private fun fetchFileName(uri: String): String {
        return uri.substring(uri.lastIndexOf("/") + 1)
    }
}