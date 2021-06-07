package com.tokopedia.oneclickcheckout.payment.list.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamRequest
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class PaymentListingFragment : BaseDaggerFragment() {

    companion object {
        private const val MERCHANT_CODE = "tokopedia"
        private const val PROFILE_CODE = "EXPRESS_SAVE"

        private const val QUERY_PARAM_EXPRESS_CHECKOUT_PARAM = "express_checkout_param"
        private const val QUERY_PARAM_USER_ID = "user_id"
        private const val QUERY_PARAM_SUCCESS = "success"
        private const val QUERY_PARAM_GATEWAY_CODE = "gateway_code"

        private const val ARG_PAYMENT_AMOUNT = "payment_amount"
        private const val ARG_ADDRESS_ID = "address_id"
        private const val ARG_PROFILE_CODE = "profile_code"

        fun newInstance(paymentAmount: Double, addressId: String, profileCode: String): PaymentListingFragment {
            val fragment = PaymentListingFragment()
            fragment.arguments = Bundle().apply {
                putDouble(ARG_PAYMENT_AMOUNT, paymentAmount)
                putString(ARG_ADDRESS_ID, addressId)
                putString(ARG_PROFILE_CODE, profileCode)
            }
            return fragment
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    @field:Named(PAYMENT_LISTING_URL)
    lateinit var paymentListingUrl: String

    private var paymentAmount = 0.0
    private var addressId = ""
    private var profileCode = ""

    private val viewModel: PaymentListingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PaymentListingViewModel::class.java]
    }

    private var webView: WebView? = null
    private var progressBar: LoaderUnify? = null
    private var globalError: GlobalError? = null

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
    }

    private fun initHeader() {
        paymentAmount = arguments?.getDouble(ARG_PAYMENT_AMOUNT) ?: 0.0
        addressId = arguments?.getString(ARG_ADDRESS_ID) ?: ""
        profileCode = arguments?.getString(ARG_PROFILE_CODE) ?: PROFILE_CODE
        val parent: Context = activity ?: return
        SplitCompat.installActivity(parent)
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
        webSettings?.mediaPlaybackRequiresUserGesture = false
    }

    private fun loadPaymentParams() {
        viewModel.paymentListingPayload.observe(viewLifecycleOwner) {
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
        }

        viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount)
    }

    private fun loadWebView(param: String) {
        webView?.postUrl(paymentListingUrl, param.toByteArray())
        webView?.visible()
        globalError?.gone()
    }

    private fun generatePaymentListingRequest(): PaymentListingParamRequest {
        return PaymentListingParamRequest(MERCHANT_CODE,
                profileCode,
                paymentListingUrl,
                addressId,
                "android-${GlobalConfig.VERSION_NAME}")
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount)
        }
        globalError?.visible()
        webView?.gone()
        progressBar?.gone()
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PaymentComponent::class.java).inject(this)
    }

    private fun goToNextStep(gatewayCode: String, metadata: String) {
        val parent = activity ?: return
        parent.setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(PaymentListingActivity.EXTRA_RESULT_GATEWAY, gatewayCode)
            putExtra(PaymentListingActivity.EXTRA_RESULT_METADATA, metadata)
        })
        parent.finish()
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
            val isSuccess = uri.getQueryParameter(QUERY_PARAM_SUCCESS)
            if (isSuccess != null && isSuccess.equals("true", true)) {
                val gatewayCode = uri.getQueryParameter(QUERY_PARAM_GATEWAY_CODE)
                if (gatewayCode != null) {
                    goToNextStep(gatewayCode, generateMetadata(uri))
                }
            }
            return super.shouldInterceptRequest(view, url)
        }

        private fun generateMetadata(uri: Uri): String {
            val map: HashMap<String, Any> = HashMap()
            for (key in uri.queryParameterNames) {
                val value = uri.getQueryParameter(key) ?: ""
                when (key) {
                    QUERY_PARAM_EXPRESS_CHECKOUT_PARAM -> {
                        try {
                            map[key] = JsonParser().parse(value)
                        } catch (e: Exception) {
                            //failed parse json string
                            map[key] = value
                        }
                    }
                    QUERY_PARAM_USER_ID -> {
                        map[key] = value.toLongOrZero()
                    }
                    QUERY_PARAM_SUCCESS -> {
                        map[key] = value.toBoolean()
                    }
                    else -> {
                        map[key] = value
                    }
                }
            }
            return Gson().toJson(map)
        }
    }
}
