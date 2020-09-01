package com.tokopedia.topchat.chatroom.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import androidx.collection.ArrayMap
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_KEY_PAYLOAD_REPORT_USER
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_KEY_REPORT_USER
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_REPORT_BLOCK_PROMO
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.RESULT_REPORT_TOASTER
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_URL
import timber.log.Timber

class TopchatReportWebViewFragment : BaseSessionWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        val uri: Uri? = Uri.parse(url)
        val queryParam = mapQueryParam(uri)

        if (url == ACTION_BLOCK_PROMO) {
            val intent = Intent().apply {
                putExtra(RESULT_KEY_REPORT_USER, RESULT_REPORT_BLOCK_PROMO)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
            return true
        } else if (uri?.host == "back" && queryParam[queryParamToasterMessage] != null) {
            val intent = Intent().apply {
                putExtra(RESULT_KEY_REPORT_USER, RESULT_REPORT_TOASTER)
                putExtra(RESULT_KEY_PAYLOAD_REPORT_USER, queryParam[queryParamToasterMessage])
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
            return true
        } else if (uri?.host == "webview" && queryParam[queryParamUrl] != null) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, queryParam[queryParamUrl])
            activity?.finish()
            return true
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

    companion object {
        const val ACTION_BLOCK_PROMO = "tkpd-internal://topchat_block_promo"
        const val queryParamToasterMessage = "show_toast_message"
        const val queryParamUrl = "url"

        fun newInstance(url: String,
                        needLogin: Boolean,
                        overrideUrl: Boolean): TopchatReportWebViewFragment {
            val fragment = TopchatReportWebViewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            args.putBoolean(KEY_NEED_LOGIN, needLogin)
            args.putBoolean(KEY_ALLOW_OVERRIDE, overrideUrl)
            fragment.arguments = args
            return fragment
        }
    }
}