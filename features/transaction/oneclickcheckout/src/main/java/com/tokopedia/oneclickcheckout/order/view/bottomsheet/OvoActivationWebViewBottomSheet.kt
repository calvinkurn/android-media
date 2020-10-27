package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.config.GlobalConfig
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.TkpdWebView

class OvoActivationWebViewBottomSheet {

    private var bottomSheetUnify: BottomSheetUnify? = null
    private var webView: TkpdWebView? = null
    private var isTopReached = true

    fun show(fragment: OrderSummaryPageFragment, userSessionInterface: UserSessionInterface) {
        val context: Context = fragment.activity ?: return
        fragment.fragmentManager?.let {
            SplitCompat.installActivity(context)
            bottomSheetUnify = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                isFullpage = true
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
//        webView.setWebChromeClient(BaseWebViewFragment.MyWebChromeClient())
//        webView.setWebViewClient(BaseWebViewFragment.MyWebViewClient())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
        if (GlobalConfig.isAllowDebuggingTools() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webView?.loadAuthUrl("${TokopediaUrl.getInstance().WEB}/ovo/api/v2/activate", userSession)
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
}