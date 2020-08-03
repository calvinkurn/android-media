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
import android.webkit.*
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_payment_method.*
import javax.inject.Inject

class CreditCardPickerFragment: BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        val phoneNumber = userSession.phoneNumber
        val msisdnVerified = userSession.isMsisdnVerified
        var phone = ""
        if (msisdnVerified && phoneNumber.isNotBlank()) {
            phone = phoneNumber
        }
        val data = "version=${GlobalConfig.VERSION_NAME}&merchant_code=tokopediatest&profile_code=EXPRESS_SAVE&enable_add_card=true&user_id=${userSession.userId}&customer_name=${userSession.name.trim()}&customer_email=${userSession.email}&customer_msisdn=${phone}&callback_url=${TokopediaUrl.getInstance().PAY}/dummy/payment/listing"
        val url = "${TokopediaUrl.getInstance().PAY}/v3/cardlist"
        web_view.postUrl(url, data.toByteArray())
    }

    inner class PaymentMethodWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            progress_bar?.gone()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress_bar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progress_bar?.gone()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            val uri = Uri.parse(url)
            val isSuccess = uri.getQueryParameter("success")
            if (isSuccess != null && isSuccess.equals("true", true)) {
                val gatewayCode = uri.getQueryParameter("gateway_code")
                if (gatewayCode != null) {
                    goToNextStep(gatewayCode, generateMetadata(uri))
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

    private fun goToNextStep(gatewayCode: String, metadata: String) {
        activity?.let {
            it.setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_RESULT_METADATA, metadata)
            })
            it.finish()
        }
    }

    companion object {
        const val EXTRA_RESULT_METADATA = "RESULT_METADATA"
    }
}