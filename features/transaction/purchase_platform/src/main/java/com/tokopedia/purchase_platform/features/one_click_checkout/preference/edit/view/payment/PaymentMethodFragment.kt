package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import kotlinx.android.synthetic.main.fragment_payment_method.*

class PaymentMethodFragment : BaseDaggerFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        web_view.clearCache(true)
        val webSettings: WebSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.domStorageEnabled = true
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = true
        web_view.webViewClient = PaymentMethodWebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.mediaPlaybackRequiresUserGesture = false
        }

        if (GlobalConfig.isAllowDebuggingTools() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        web_view.loadUrl("https://pay-staging.tokopedia.com/dummy/payment/listing")
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
    }

    override fun onStart() {
        super.onStart()
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.setStepperValue(75, true)
        }
    }

    inner class PaymentMethodWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress_bar.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progress_bar.gone()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val r1 = request
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            val r = request
            return super.shouldInterceptRequest(view, request)
        }

        override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
            val m1 = dontResend
            val m2 = resend
            super.onFormResubmission(view, dontResend, resend)
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.addFragment(ShippingDurationFragment())
            parent.setStepperValue(100, true)
        }
    }

}
