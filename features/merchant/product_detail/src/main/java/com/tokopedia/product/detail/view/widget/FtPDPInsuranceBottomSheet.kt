package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.webview.TkpdWebView

class FtPDPInsuranceBottomSheet : BottomSheetUnify(){

    companion object {
        const val KEY_SPONSOR_URL = "sponsorUrl"
    }

    //private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var url: String = ""
    private lateinit var childView: View
    private val childLayoutRes = R.layout.widget_bottomsheet_protection_info
    private lateinit var webView: TkpdWebView
    private lateinit var progressBar: LoaderUnify


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(KEY_SPONSOR_URL)) {
                setDefaultParams()
                url = it.getString(KEY_SPONSOR_URL) ?: ""
                childView = LayoutInflater.from(context).inflate(childLayoutRes,
                        null, false)
                setChild(childView)
            } else {
                dismiss()
            }
            initViews()
        }
    }

    private fun initViews() {
        webView = childView.findViewById<TkpdWebView>(R.id.swd_tnc_webview)
        progressBar = childView.findViewById<LoaderUnify>(R.id.progressBar)
        configWebView()
    }

    private fun configWebView() {
        webView.run {
            webChromeClient = MyChromeClient()
            webViewClient = MyWebViewClient()
        }
        webView.settings.apply {
            domStorageEnabled = true
            javaScriptEnabled = true
        }
        webView.loadUrl(url)
    }


    private fun setDefaultParams() {
        isDragable = true
        showCloseIcon = false
        showKnob = true
        showHeader = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet: FrameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bottomSheet)
            //behavior.skipCollapsed = true
            behavior.isHideable = true
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        return bottomSheetDialog
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar.gone()
            webView.visible()
        }
    }

    private inner class MyChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                progressBar.gone()
                webView.visible()
            }
            super.onProgressChanged(view, newProgress)
        }
    }

}
