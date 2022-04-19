package com.tokopedia.product.detail.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.webview.TkpdWebView

class FtPDPInsuranceBottomSheet : BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.widget_bottomsheet_protection_info
    private var webView: TkpdWebView? = null
    private var progressBar: LoaderUnify? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(KEY_SPONSOR_URL)) {
                setDefaultParams()
                val url = it.getString(KEY_SPONSOR_URL) ?: ""
                val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                        null, false)
                setChild(childView)
                initViews(childView)
                configWebView(url)
            } else {
                dismiss()
            }
        }
    }

    private fun initViews(childView: View) {
        webView = childView.findViewById(R.id.ppInsuranceWebview)
        progressBar = childView.findViewById(R.id.progressBar)
    }

    private fun configWebView(url: String) {
        webView?.run {
            webChromeClient = MyChromeClient()
            webViewClient = MyWebViewClient()
            settings.apply {
                domStorageEnabled = true
                javaScriptEnabled = true
            }
            loadUrl(url)
        }
    }


    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showKnob = true
        showHeader = false
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.gone()
            webView?.visible()
        }
    }

    private inner class MyChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                progressBar?.gone()
                webView?.visible()
            }
            super.onProgressChanged(view, newProgress)
        }
    }

    companion object {
        const val KEY_SPONSOR_URL = "sponsorUrl"
        const val TAG = "FT_TAG"

        fun show(sponsorUrl: String, childFragmentManager: FragmentManager) {
            val pdpInsuranceBottomSheet =  FtPDPInsuranceBottomSheet().apply {
                val bundleData = Bundle().apply {
                    putString(KEY_SPONSOR_URL, sponsorUrl)
                }
                arguments = bundleData
            }
            pdpInsuranceBottomSheet.show(childFragmentManager, TAG)
        }
    }

}
