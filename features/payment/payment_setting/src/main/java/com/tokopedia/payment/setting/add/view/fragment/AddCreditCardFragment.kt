package com.tokopedia.payment.setting.add.view.fragment

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.payment.setting.add.view.activity.AddCreditCardActivity
import com.tokopedia.payment.setting.list.model.PaymentSignature
import com.tokopedia.payment.setting.util.GET_IFRAME_ADD_CC_URL
import com.tokopedia.payment.setting.util.PAYMENT_SETTING_URL
import com.tokopedia.webview.BaseWebViewFragment
import java.net.URLEncoder

class AddCreditCardFragment : BaseWebViewFragment() {

    private var callbackUrl: String = ""

    private lateinit var paymentSignature: PaymentSignature

    override fun getUrl() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(AddCreditCardActivity.ARG_PAYMENT_SIGNATURE)) {
                paymentSignature = it.getParcelable(AddCreditCardActivity.ARG_PAYMENT_SIGNATURE)!!
            }
        }
        activity?.run { NetworkClient.init(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSuccessGetIFrameData(paymentSignature)
    }

    private fun onSuccessGetIFrameData(paymentSignature: PaymentSignature) {
        loadWeb()
        webView.postUrl("$PAYMENT_SETTING_URL$GET_IFRAME_ADD_CC_URL", getURLQuery().toByteArray())
        callbackUrl = paymentSignature.callbackUrl
    }

    private fun getURLQuery(): String {
        return "$MERCHANT_CODE=${getUrlEncoded(paymentSignature.merchantCode)}&" +
                "$PROFILE_CODE=${getUrlEncoded(paymentSignature.profileCode)}&" +
                "$IP_ADDRESS=${getUrlEncoded(paymentSignature.ipAddress)}&" +
                "$DATE=${getUrlEncoded(paymentSignature.date)}&" +
                "$USER_ID=${getUrlEncoded(paymentSignature.userId.toString())}&" +
                "$CUSTOMER_NAME=${getUrlEncoded(paymentSignature.customerName)}&" +
                "$CUSTOMER_EMAIL=${getUrlEncoded(paymentSignature.customerEmail)}&" +
                "$CALLBACK_URL=${getUrlEncoded(paymentSignature.callbackUrl)}&" +
                "$CUSTOMER_MSISDN=${getUrlEncoded(paymentSignature.customerMsisdn)}&" +
                "$SIGNATURE=${getUrlEncoded(paymentSignature.hash)}"
    }

    private fun getUrlEncoded(valueStr: String) = URLEncoder.encode(valueStr, ENCODING_UTF_8)

    override fun initInjector() {
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (callbackUrl == "") {
            return false
        }
        if (url == callbackUrl) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun webViewClientShouldInterceptRequest(view: WebView?, request: WebResourceRequest?) {
        if (request?.url?.toString()?.equals(callbackUrl) == true) {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    companion object {
        const val ENCODING_UTF_8 = "UTF-8"
        const val MERCHANT_CODE = "merchant_code"
        const val PROFILE_CODE = "profile_code"
        const val IP_ADDRESS = "ip_address"
        const val DATE = "date"
        const val USER_ID = "user_id"
        const val CUSTOMER_NAME = "customer_name"
        const val CUSTOMER_EMAIL = "customer_email"
        const val CALLBACK_URL = "callback_url"
        const val CUSTOMER_MSISDN = "customer_msisdn"
        const val SIGNATURE = "signature"

        fun createInstance(bundle: Bundle?) = AddCreditCardFragment().apply {
            if (bundle != null)
                arguments = bundle
        }
    }
}