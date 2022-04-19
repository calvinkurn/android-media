package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.oneclickcheckout.databinding.BottomSheetPurchaseProtectionInfoWebViewBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp

class PurchaseProtectionInfoBottomsheet(private val url: String) {

    private var binding: BottomSheetPurchaseProtectionInfoWebViewBinding? = null

    fun show(fragment: OrderSummaryPageFragment) {
        fragment.parentFragmentManager.let {
            BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                showCloseIcon = false
                showHeader = false
                clearContentPadding = true
                showKnob = true

                binding = BottomSheetPurchaseProtectionInfoWebViewBinding.inflate(LayoutInflater.from(fragment.context))
                setupChild()

                customPeekHeight = (getScreenHeight() / 2).toDp()

                setChild(binding?.root)
                setOnDismissListener {
                    binding = null
                }
                show(it, null)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupChild() {
        val webSettings = binding?.webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
            setAppCacheEnabled(true)
        }
        binding?.webView?.webViewClient = PurchaseProtectionInfoWebViewClient()
        webSettings?.mediaPlaybackRequiresUserGesture = false

        binding?.webView?.loadUrl(url)
    }

    inner class PurchaseProtectionInfoWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding?.progressBar?.gone()
        }
    }

}