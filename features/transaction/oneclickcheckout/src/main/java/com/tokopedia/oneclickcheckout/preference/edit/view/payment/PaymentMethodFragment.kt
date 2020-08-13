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
import android.webkit.SslErrorHandler
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.ListingParam
import com.tokopedia.oneclickcheckout.preference.edit.data.payment.PaymentListingParamRequest
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.summary.PreferenceSummaryFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.net.UnknownHostException
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PaymentMethodViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PaymentMethodViewModel::class.java]
    }

    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var globalError: GlobalError? = null

    private var param: ListingParam? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initHeader()
        initWebView()
        loadPaymentParams()
    }

    private fun initViews(view: View) {
        webView = view.findViewById(R.id.web_view)
        progressBar = view.findViewById(R.id.progress_bar)
        globalError = view.findViewById(R.id.global_error)

//        view.findViewById<Button>(R.id.btntesting).setOnClickListener {
//            param?.let {
//                val url = "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
//                webView?.postUrl(url, getPayload(it).toByteArray())
//            }
//        }
//{"success":false,"message":"invalid signature. got: abac453245db4029410ccd8f34c7ca28923824b3 | expected: e2eb01d1543f827d2d07cd3a95891e938befface","data":{"url":"","method":"","form":null}}
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
                parent.setStepperValue(75)
            }
        }
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
    }

    private fun loadPaymentParams() {
        viewModel.paymentListingParam.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.Success -> {
                    loadWebView(it.data)
                }
                is OccState.Failed -> {
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }
                is OccState.Loading -> {
                    progressBar?.visible()
                    globalError?.gone()
                    webView?.gone()
                }
            }
        })

        viewModel.getPaymentListingParam(generatePaymentListingRequest())
    }

    private fun loadWebView(param: ListingParam) {
        this.param = param
        val url = "${TokopediaUrl.getInstance().PAY}/v2/payment/register/listing"
        webView?.postUrl(url, getPayload(param).toByteArray())
//        webView?.loadUrl("https://www.google.com")
        webView?.visible()
        globalError?.gone()
    }

    private fun getPayload(param: ListingParam): String {
        return "merchant_code=${getUrlEncoded(param.merchantCode)}&" +
                "profile_code=${getUrlEncoded(param.profileCode)}&" +
                "user_id=${getUrlEncoded(param.userId)}&" +
                "customer_name=${getUrlEncoded(param.customerName)}&" +
                "customer_email=${getUrlEncoded(param.customerEmail)}&" +
                "customer_msisdn=${getUrlEncoded(param.customerMsisdn)}&" +
                "address_id=${getUrlEncoded(param.addressId)}&" +
                "callback_url=${getUrlEncoded(param.callbackUrl)}&" +
                "version=${getUrlEncoded("android-${GlobalConfig.VERSION_NAME}")}&" +
                "signature=${getUrlEncoded(param.hash)}"
    }

    private fun generatePaymentListingRequest(): PaymentListingParamRequest {
        return PaymentListingParamRequest("tokopedia",
                "EXPRESS_SAVE",
                "${TokopediaUrl.getInstance().PAY}/dummy/payment/listing",
                getAddressId(),
                "android-${GlobalConfig.VERSION_NAME}")
    }

    private fun getAddressId(): String {
        val parent = activity
        if (parent is PreferenceEditParent) {
            return parent.getAddressId().toString()
        }
        return ""
    }

    private fun getUrlEncoded(valueStr: String): String {
        return URLEncoder.encode(valueStr, "UTF-8")
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            viewModel.getPaymentListingParam(generatePaymentListingRequest())
        }
        globalError?.visible()
        webView?.gone()
        progressBar?.gone()
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
            parent.setStepperValue(75)
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

    override fun onDestroyView() {
        super.onDestroyView()
        webView = null
        progressBar = null
        globalError = null
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
}
