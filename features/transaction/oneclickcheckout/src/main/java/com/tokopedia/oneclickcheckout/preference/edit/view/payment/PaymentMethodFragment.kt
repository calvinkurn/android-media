package com.tokopedia.oneclickcheckout.preference.edit.view.payment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.summary.PreferenceSummaryFragment
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_payment_method.*
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class PaymentMethodFragment : BaseDaggerFragment() {

    companion object {

        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): PaymentMethodFragment {
            val paymentMethodFragment = PaymentMethodFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            paymentMethodFragment.arguments = bundle
            return paymentMethodFragment
        }
    }

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val compositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onDestroyView() {
        compositeSubscription.clear()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initWebView()
    }

    private fun initHeader() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            parent.hideAddButton()
            parent.hideDeleteButton()
            val parentContext: Context = parent
            SplitCompat.installActivity(parentContext)
            parent.setHeaderTitle(parentContext.getString(R.string.lbl_choose_payment_method))
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.hideStepper()
            } else {
                parent.setHeaderSubtitle(parentContext.getString(R.string.step_choose_payment))
                parent.showStepper()
                parent.setStepperValue(75, true)
            }
        }
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

        var addressId = ""
        val parent = activity
        if (parent is PreferenceEditParent) {
            addressId = parent.getAddressId().toString()
        }
        val phoneNumber = userSession.phoneNumber
        val msisdnVerified = userSession.isMsisdnVerified
        var phone = ""
        if (msisdnVerified && phoneNumber.isNotBlank()) {
            phone = phoneNumber
        }
        val data = "merchant_code=tokopediatest&profile_code=EXPRESS_SAVE&user_id=${userSession.userId}&customer_name=${userSession.name.trim()}&customer_email=${userSession.email}&customer_msisdn=${phone}&address_id=${addressId}&callback_url=${TokopediaUrl.getInstance().PAY}/dummy/payment/listing"
        val url = "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
        web_view.postUrl(url, data.toByteArray())
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onStart() {
        super.onStart()
        val parent = activity
        if (parent is PreferenceEditParent) {
            parent.setStepperValue(75, true)
        }
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
                    preferenceListAnalytics.eventClickPaymentMethodOptionInPilihMetodePembayaranPage(gatewayCode)
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
        val parent = activity
        if (parent is PreferenceEditParent) {
            parent.setGatewayCode(gatewayCode)
            parent.setPaymentQuery(metadata)
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.goBack()
            } else {
                parent.addFragment(PreferenceSummaryFragment.newInstance())
            }
        }
    }

}
