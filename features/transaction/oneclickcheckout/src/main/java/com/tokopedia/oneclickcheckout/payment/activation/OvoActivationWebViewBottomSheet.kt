package com.tokopedia.oneclickcheckout.payment.activation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.view.LayoutInflater
import android.webkit.*
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.BottomSheetWebViewBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.ext.encodeOnce
import timber.log.Timber

class OvoActivationWebViewBottomSheet(private val activationUrl: String,
                                      private val callbackUrl: String,
                                      private val listener: OvoActivationWebViewBottomSheetListener) {

    private var context: Context? = null

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var binding: BottomSheetWebViewBinding? = null
    private var isDone = false

    companion object {
        private const val BACK_APPLINK = "tokopedia://back"

        private const val IS_SUCCESS_QUERY = "is_success"
        private const val SUCCESS_QUERY_VALUE = "1"

        private const val REDIRECT_URL_QUERY = "redirect_url"
        private const val TOKOPEDIA_URL = "tokopedia"
    }

    fun show(fragment: OrderSummaryPageFragment, userSessionInterface: UserSessionInterface) {
        val context: Context = fragment.activity ?: return
        this.context = context
        fragment.parentFragmentManager.let {
            SplitCompat.installActivity(context)
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                isFullpage = true
                isKeyboardOverlap = false
                showCloseIcon = true
                showHeader = true
                clearContentPadding = true
                setTitle(fragment.getString(R.string.lbl_activate_ovo_now))

                binding = BottomSheetWebViewBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild(userSessionInterface)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(binding?.root)
                setOnDismissListener {
                    this@OvoActivationWebViewBottomSheet.context = null
                    binding = null
                }
                show(it, null)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupChild(userSession: UserSessionInterface) {
        binding?.webView?.clearCache(true)
        val webSettings = binding?.webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
        }
        binding?.webView?.webViewClient = OvoActivationWebViewClient()
        webSettings?.mediaPlaybackRequiresUserGesture = false

        loadOvoActivationWebView(userSession)
    }

    private fun loadOvoActivationWebView(userSession: UserSessionInterface) {
        // Load auth only if tokopedia url
        if (activationUrl.contains(TOKOPEDIA_URL)) {
            binding?.webView?.loadAuthUrl(generateUrl(userSession), userSession)
        } else {
            binding?.webView?.loadUrl(activationUrl)
        }
    }

    private fun generateUrl(userSession: UserSessionInterface): String {
        return URLGenerator.generateURLSessionLogin(
                "$activationUrl?$REDIRECT_URL_QUERY=${generateRedirectUrl()}".encodeOnce(),
                userSession.deviceId,
                userSession.userId)
    }

    private fun generateRedirectUrl(): String {
        return callbackUrl
    }

    private fun onActivationResult(isSuccess: Boolean) {
        if (!isDone) {
            isDone = true
            bottomSheetUnify?.dismiss()
            listener.onActivationResult(isSuccess)
            context = null
            binding = null
            bottomSheetUnify = null
        }
    }

    private fun onMoveToIntent(link: String) {
        if (!isDone) {
            context?.let {
                try {
                    isDone = true
                    bottomSheetUnify?.dismiss()
                    it.startActivity(RouteManager.getIntent(it, link))
                } catch (t: Throwable) {
                    // ignore
                    Timber.d(t)
                }
            }
        }
    }

    private fun onMoveBack() {
        if (!isDone) {
            context?.let {
                try {
                    isDone = true
                    bottomSheetUnify?.dismiss()
                } catch (t: Throwable) {
                    // ignore
                    Timber.d(t)
                }
            }
        }
    }

    inner class OvoActivationWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // moved here because it is "GET" operation
            if (url != null && url.startsWith(generateRedirectUrl(), true)) {
                val uri = Uri.parse(url)
                val isSuccessResult = uri.getQueryParameter(IS_SUCCESS_QUERY) == SUCCESS_QUERY_VALUE
                onActivationResult(isSuccessResult)
            } else if (url == BACK_APPLINK) {
                onMoveBack()
            } else if (url != null && !URLUtil.isNetworkUrl(url)) {
                onMoveToIntent(url)
            }
            binding?.progressBar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding?.progressBar?.gone()
        }
    }

    interface OvoActivationWebViewBottomSheetListener {

        fun onActivationResult(isSuccess: Boolean)
    }
}