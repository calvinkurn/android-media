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
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_BACK_BUTTON_APPLINK
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_SOURCE_PAYMENT
import com.tokopedia.common.payment.utils.LinkStatusMatcher
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.PAYMENT_LISTING_URL
import com.tokopedia.oneclickcheckout.common.utils.generateAppVersionForPayment
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.databinding.FragmentPaymentMethodBinding
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.oneclickcheckout.payment.list.data.PaymentListingParamRequest
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class PaymentListingFragment : BaseDaggerFragment() {

    companion object {
        private const val QUERY_PARAM_EXPRESS_CHECKOUT_PARAM = "express_checkout_param"
        private const val QUERY_PARAM_USER_ID = "user_id"
        private const val QUERY_PARAM_SUCCESS = "success"
        private const val QUERY_PARAM_GATEWAY_CODE = "gateway_code"

        private const val ARG_PAYMENT_AMOUNT = "payment_amount"
        private const val ARG_ADDRESS_ID = "address_id"
        private const val ARG_PROFILE_CODE = "profile_code"
        private const val ARG_MERCHANT_CODE = "merchant_code"
        private const val ARG_PAYMENT_BID = "bid"
        private const val ARG_ORDER_METADATA = "order_metadata"

        private const val REQUEST_CODE_LINK_ACCOUNT = 101
        private const val REQUEST_CODE = 191

        fun newInstance(paymentAmount: Double, addressId: String, profileCode: String,
                        merchantCode: String, bid: String, orderMetadata: String): PaymentListingFragment {
            val fragment = PaymentListingFragment()
            fragment.arguments = Bundle().apply {
                putDouble(ARG_PAYMENT_AMOUNT, paymentAmount)
                putString(ARG_ADDRESS_ID, addressId)
                putString(ARG_PROFILE_CODE, profileCode)
                putString(ARG_MERCHANT_CODE, merchantCode)
                putString(ARG_PAYMENT_BID, bid)
                putString(ARG_ORDER_METADATA, orderMetadata)
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
    private var merchantCode = ""
    private var bid = ""
    private var orderMetadata = ""

    private val viewModel: PaymentListingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PaymentListingViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentPaymentMethodBinding>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LINK_ACCOUNT && view != null) {
            if (resultCode == Activity.RESULT_OK) {
                val status = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_STATUS) ?: ""
                if (status.isNotEmpty()) {
                    handleStatusMatching(status)
                }
            }
            viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount, orderMetadata)
        } else if (requestCode == REQUEST_CODE && view != null) {
            viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount, orderMetadata)
        }
    }

    private fun handleStatusMatching(status: String) {
        val message = LinkStatusMatcher.getStatus(status)
        val v = view
        if (message.isNotEmpty() && v != null) {
            Toaster.build(v, message, Toaster.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initWebView()
        loadPaymentParams()
    }

    private fun initHeader() {
        paymentAmount = arguments?.getDouble(ARG_PAYMENT_AMOUNT) ?: 0.0
        addressId = arguments?.getString(ARG_ADDRESS_ID) ?: ""
        profileCode = arguments?.getString(ARG_PROFILE_CODE) ?: ""
        merchantCode = arguments?.getString(ARG_MERCHANT_CODE) ?: ""
        bid = arguments?.getString(ARG_PAYMENT_BID) ?: ""
        orderMetadata = arguments?.getString(ARG_ORDER_METADATA) ?: ""
        val parent: Context = activity ?: return
        SplitCompat.installActivity(parent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding?.apply {
            webView.clearCache(true)
            val webSettings = webView.settings
            webSettings?.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                builtInZoomControls = false
                displayZoomControls = true
//                setAppCacheEnabled(true)
            }
            webView.webViewClient = PaymentMethodWebViewClient()
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
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
                else -> {
                    binding?.apply {
                        progressBar.visible()
                        globalError.gone()
                        webView.gone()
                    }
                }
            }
        }

        viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount, orderMetadata)
    }

    private fun loadWebView(param: String) {
        binding?.apply {
            webView.postUrl(paymentListingUrl, param.toByteArray())
            webView.visible()
            globalError.gone()
        }
    }

    private fun generatePaymentListingRequest(): PaymentListingParamRequest {
        return PaymentListingParamRequest(
            merchantCode,
            profileCode,
            paymentListingUrl,
            addressId,
            generateAppVersionForPayment(),
            bid
        )
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
        binding?.apply {
            globalError.setType(type)
            globalError.setActionClickListener {
                viewModel.getPaymentListingPayload(generatePaymentListingRequest(), paymentAmount, orderMetadata)
            }
            globalError.visible()
            webView.gone()
            progressBar.gone()
        }
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

    private fun goToLinkAccount(context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW).apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_LD, LINK_ACCOUNT_BACK_BUTTON_APPLINK)
            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, LINK_ACCOUNT_SOURCE_PAYMENT)
        }
        startActivityForResult(intent, REQUEST_CODE_LINK_ACCOUNT)
    }

    inner class PaymentMethodWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            binding?.progressBar?.gone()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding?.progressBar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding?.progressBar?.gone()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            // applink
            val url = request?.url?.toString() ?: ""
            context?.let {
                if (url.isNotEmpty() && url.startsWith(ApplinkConst.LINK_ACCOUNT)) {
                    goToLinkAccount(it)
                    return true
                }
                if (!URLUtil.isNetworkUrl(url) && RouteManager.isSupportApplink(it, url)) {
                    val intent = RouteManager.getIntent(it, url).apply {
                        data = Uri.parse(url)
                    }
                    startActivityForResult(intent, REQUEST_CODE)
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
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
