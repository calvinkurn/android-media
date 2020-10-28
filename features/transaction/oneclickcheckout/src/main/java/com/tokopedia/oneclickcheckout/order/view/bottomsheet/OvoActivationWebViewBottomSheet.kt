package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.webview.ext.encodeOnce
import timber.log.Timber

class OvoActivationWebViewBottomSheet(private val callbackUrl: String,
                                      private val listener: OvoActivationWebViewBottomSheetListener) {

    private var context: Context? = null

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var webView: TkpdWebView? = null
    private var progressBar: LoaderUnify? = null
    private var isTopReached = true
    private var isDone = false

    companion object {
        private const val REDIRECT_URL_ENDPOINT = "occ"
    }

    fun show(fragment: OrderSummaryPageFragment, userSessionInterface: UserSessionInterface) {
        val context: Context = fragment.activity ?: return
        this.context = context
        fragment.fragmentManager?.let {
            SplitCompat.installActivity(context)
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                isKeyboardOverlap = false
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.lbl_activate_ovo_now))

                val child = View.inflate(fragment.context, R.layout.bottom_sheet_web_view, null)
                setupChild(userSessionInterface, child)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                setCloseClickListener {
                    webView?.loadAuthUrl(generateUrl(userSessionInterface), userSessionInterface)
                }
                setShowListener {
                    this.bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(p0: View, p1: Int) {
                            if (p1 == BottomSheetBehavior.STATE_DRAGGING && !isTopReached) {
                                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
                            } else if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                                dismiss()
                            }
                        }

                        override fun onSlide(p0: View, p1: Float) {

                        }
                    })
                }
                show(it, null)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupChild(userSession: UserSessionInterface, child: View) {
        progressBar = child.findViewById(R.id.progress_bar)
        webView = child.findViewById(R.id.web_view)
        webView?.clearCache(true)
        val webSettings = webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
        }
        webView?.webViewClient = OvoActivationWebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
        if (GlobalConfig.isAllowDebuggingTools() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView?.loadUrl("https://www.bing.com")
//        webView?.loadAuthUrl("${TokopediaUrl.getInstance().WEB}/ovo/api/v2/activate", userSession)
        webView?.setWebViewScrollListener(object : TkpdWebView.WebviewScrollListener {
            override fun onTopReached() {
                isTopReached = true
            }

            override fun onEndReached() {

            }

            override fun onHasScrolled() {
                isTopReached = false
            }
        })
    }

    private fun generateUrl(userSession: UserSessionInterface): String {
        return URLGenerator.generateURLSessionLogin(
                "${TokopediaUrl.getInstance().WEB}ovo/api/v2/activate?redirect_url=${generateRedirectUrl()}".encodeOnce(),
                userSession.deviceId,
                userSession.userId)
    }

    private fun generateRedirectUrl(): String {
        return "https://www.google.com"
    }

    private fun onActivationResult(isSuccess: Boolean) {
        if (!isDone) {
            isDone = true
            bottomSheetUnify?.dismiss()
            listener.onActivationResult(isSuccess)
            context = null
            webView = null
            progressBar = null
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

    inner class OvoActivationWebViewClient: WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.i("qwertyuiop", "start url ${url ?: "none"}")
            progressBar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.gone()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            Log.i("qwertyuiop", "intercept url ${url ?: "none"}")
            if (url != null && (url.contains(callbackUrl, true) || url.contains(generateRedirectUrl(), true))) {
                val uri = Uri.parse(url)
                val isSuccessResult = uri.getQueryParameter("is_success") == "1"
                onActivationResult(isSuccessResult)
            } else if (url != null && !URLUtil.isNetworkUrl(url)) {
                onMoveToIntent(url)
            }
            return super.shouldInterceptRequest(view, url)
        }
    }

    interface OvoActivationWebViewBottomSheetListener {

        fun onActivationResult(isSuccess: Boolean)
    }
}