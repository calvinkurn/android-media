package com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import java.net.URLEncoder

class CreditCardPickerFragment : BaseDaggerFragment() {

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        // no op
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initWebView()
        loadWebView()
    }

    private fun initViews(view: View) {
        webView = view.findViewById(R.id.web_view)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView?.clearCache(true)
        val webSettings = webView?.settings
        webSettings?.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
            setAppCacheEnabled(true)
        }
        webView?.webViewClient = PaymentMethodWebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView?.visible()
    }

    private fun loadWebView() {
        val additionalData = arguments?.getParcelable<OrderPaymentCreditCardAdditionalData>(EXTRA_ADDITIONAL_DATA)
        if (additionalData == null) {
            activity?.finish()
            return
        }
        webView?.postUrl(additionalData.changeCcLink, getPayload(additionalData).toByteArray())
    }

    private fun getPayload(additionalData: OrderPaymentCreditCardAdditionalData): String {
        return "merchant_code=${getUrlEncoded(additionalData.merchantCode)}&" +
                "profile_code=${getUrlEncoded(additionalData.profileCode)}&" +
                "enable_add_card=${getUrlEncoded("false")}&" +
                "user_id=${getUrlEncoded(additionalData.id.toString())}&" +
                "customer_name=${getUrlEncoded(additionalData.name.trim())}&" +
                "customer_email=${getUrlEncoded(additionalData.email)}&" +
                "customer_msisdn=${getUrlEncoded(additionalData.msisdn)}&" +
                "signature=${getUrlEncoded(additionalData.signature)}&" +
                "callback_url=${getUrlEncoded(additionalData.callbackUrl)}"
    }

    private fun getUrlEncoded(valueStr: String): String {
        return URLEncoder.encode(valueStr, "UTF-8")
    }

    inner class PaymentMethodWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            progressBar?.gone()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progressBar?.gone()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            val uri = Uri.parse(url)
            val isSuccess = uri.getQueryParameter("success")
            if (isSuccess != null && isSuccess.equals("true", true)) {
                val gatewayCode = uri.getQueryParameter("gateway_code")
                if (gatewayCode != null) {
                    goToNextStep(generateMetadata(uri))
                }
            }
            return super.shouldInterceptRequest(view, url)
        }

        private fun generateMetadata(uri: Uri): String {
            val map: HashMap<String, String> = HashMap()
            for (key in uri.queryParameterNames) {
                map[key] = uri.getQueryParameter(key) ?: ""
            }
            return Gson().toJson(map)
        }
    }

    private fun goToNextStep(metadata: String) {
        activity?.let {
            it.setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_RESULT_METADATA, metadata)
            })
            it.finish()
        }
    }

    companion object {
        const val EXTRA_RESULT_METADATA = "RESULT_METADATA"

        const val EXTRA_ADDITIONAL_DATA = "additional_data"

        fun createInstance(additionalData: OrderPaymentCreditCardAdditionalData): CreditCardPickerFragment {
            return CreditCardPickerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_ADDITIONAL_DATA, additionalData)
                }
            }
        }
    }
}