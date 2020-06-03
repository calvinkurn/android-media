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

    override fun getUrl(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(AddCreditCardActivity.ARG_PAYMENT_SIGNATURE)) {
                paymentSignature = it.getParcelable(AddCreditCardActivity.ARG_PAYMENT_SIGNATURE)
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
        webView.postUrl("$PAYMENT_SETTING_URL$GET_IFRAME_ADD_CC_URL", getIFrameUrlStr().toByteArray())
        callbackUrl = paymentSignature.callbackUrl
    }

    private fun getIFrameUrlStr(): String {
        return "$MERCHANT_CODE=${URLEncoder.encode(this.paymentSignature.merchantCode, "UTF-8")}&" +
                "$PROFILE_CODE=${URLEncoder.encode(this.paymentSignature.profileCode, "UTF-8")}&" +
                "$IP_ADDRESS=${URLEncoder.encode(this.paymentSignature.ipAddress, "UTF-8")}&" +
                "$DATE=${URLEncoder.encode(this.paymentSignature.date, "UTF-8")}&" +
                "$USER_ID=${URLEncoder.encode(this.paymentSignature.userId.toString(), "UTF-8")}&" +
                "$CUSTOMER_NAME=${URLEncoder.encode(this.paymentSignature.customerName, "UTF-8")}&" +
                "$CUSTOMER_EMAIL=${URLEncoder.encode(this.paymentSignature.customerEmail, "UTF-8")}&" +
                "$CALLBACK_URL=${URLEncoder.encode(this.paymentSignature.callbackUrl, "UTF-8")}&" +
                "$CUSTOMER_MSISDN=${URLEncoder.encode(this.paymentSignature.customerMsisdn, "UTF-8")}&" +
                "$SIGNATURE=${URLEncoder.encode(this.paymentSignature.hash, "UTF-8")}"
    }

    override fun initInjector() {
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        if (callbackUrl == "") {
            return false
        }
        if (url.equals(callbackUrl)) {
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
        fun createInstance(bundle: Bundle): AddCreditCardFragment {
            return AddCreditCardFragment().apply {
                arguments = bundle
            }
        }
    }
}