package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.webview.ext.encodeOnce

class OvoActivationWebViewBottomSheet(private val callbackUrl: String, private val listener: OvoActivationWebViewBottomSheetListener) {

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var webView: TkpdWebView? = null
    private var isTopReached = true

    companion object {
        private const val REDIRECT_URL_ENDPOINT = "occ"
    }

    fun show(fragment: OrderSummaryPageFragment, userSessionInterface: UserSessionInterface) {
        val context: Context = fragment.activity ?: return
        fragment.fragmentManager?.let {
            SplitCompat.installActivity(context)
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                isKeyboardOverlap = false
                showCloseIcon = true
                showHeader = true
                setTitle(fragment.getString(R.string.lbl_choose_installment_type))

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

        webView?.loadUrl("https://www.google.com")
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
        return "${TokopediaUrl.getInstance().WEB}$REDIRECT_URL_ENDPOINT"
    }

    inner class OvoActivationWebViewClient: WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            if (url?.contains(generateRedirectUrl()) == true) {
                val uri = Uri.parse(url)
                val isSuccessResult = uri.getQueryParameter("is_success") == "1"
                listener.onActivationResult(isSuccessResult)
                bottomSheetUnify?.dismiss()
            }
            return super.shouldInterceptRequest(view, url)
        }
    }

    interface OvoActivationWebViewBottomSheetListener {

        fun onActivationResult(isSuccess: Boolean)
    }
}