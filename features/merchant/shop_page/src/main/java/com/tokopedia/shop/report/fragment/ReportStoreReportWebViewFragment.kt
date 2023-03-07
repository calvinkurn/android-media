package com.tokopedia.shop.report.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.collection.ArrayMap
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment.Companion.RESULT_KEY_PAYLOAD_REPORT_USER
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment.Companion.RESULT_KEY_REPORT_USER
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment.Companion.RESULT_REPORT_TOASTER
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_URL
import timber.log.Timber

class ReportStoreReportWebViewFragment : BaseSessionWebViewFragment() {

    companion object {
        const val QUERY_PARAM_TOASTER_MESSAGE = "show_toast_message"
        const val URI_HOST_KEY = "back"

        fun newInstance(
            url: String,
            overrideUrl: Boolean
        ): ReportStoreReportWebViewFragment {
            val fragment = ReportStoreReportWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val uri: Uri? = Uri.parse(url)
        val queryParam = mapQueryParam(uri)

        when {
            uri?.host == URI_HOST_KEY && queryParam[QUERY_PARAM_TOASTER_MESSAGE] != null -> {
                val intent = Intent().apply {
                    putExtra(RESULT_KEY_REPORT_USER, RESULT_REPORT_TOASTER)
                    putExtra(RESULT_KEY_PAYLOAD_REPORT_USER, queryParam[QUERY_PARAM_TOASTER_MESSAGE])
                }
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
                return true
            }
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    private fun mapQueryParam(uri: Uri?): ArrayMap<String, String> {
        val map = ArrayMap<String, String>()
        if (uri == null) return map
        try {
            val keys = uri.queryParameterNames
            for (key in keys) {
                map[key] = uri.getQueryParameter(key)
            }
        } catch (e: UnsupportedOperationException) {
            Timber.d(e)
        } catch (e: NullPointerException) {
            Timber.d(e)
        } catch (e: Exception) {
            Timber.d(e)
        }
        return map
    }
}
