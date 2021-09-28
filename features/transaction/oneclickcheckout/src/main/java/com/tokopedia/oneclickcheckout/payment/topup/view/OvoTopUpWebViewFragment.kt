package com.tokopedia.oneclickcheckout.payment.topup.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.databinding.FragmentPaymentWebViewBinding
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class OvoTopUpWebViewFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: OvoTopUpWebViewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OvoTopUpWebViewViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentPaymentWebViewBinding>()

    private var redirectUrl: String? = null

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PaymentComponent::class.java).inject(this)
    }

    private fun getRedirectUrl(): String {
        if (redirectUrl == null) {
            redirectUrl = arguments?.getString(EXTRA_REDIRECT_URL, "") ?: ""
        }
        return redirectUrl!!
    }

    private fun isHideDigital(): String {
        return arguments?.getInt(EXTRA_IS_HIDE_DIGITAL, 0)?.toString() ?: "0"
    }

    private fun getCustomerData(): OrderPaymentOvoCustomerData {
        return arguments?.getParcelable(EXTRA_CUSTOMER_DATA) ?: OrderPaymentOvoCustomerData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentWebViewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView()

        observeOvoTopUpUrl()

        viewModel.getOvoTopUpUrl(getRedirectUrl(), getCustomerData())
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
                setAppCacheEnabled(true)
            }
            webView.webViewClient = OvoTopUpWebViewClient()
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
    }

    private fun observeOvoTopUpUrl() {
        viewModel.ovoTopUpUrl.observe(viewLifecycleOwner, {
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
        })
    }

    private fun loadWebView(url: String) {
        binding?.apply {
            val newUrl = Uri.parse(url).buildUpon().appendQueryParameter(QUERY_IS_HIDE_DIGITAL, isHideDigital()).build().toString()
            webView.loadAuthUrl(generateUrl(newUrl, userSession), userSession)
            webView.visible()
            globalError.gone()
        }
    }

    private fun generateUrl(url: String, userSession: UserSessionInterface): String {
        // Uri should automatically encode
        return URLGenerator.generateURLSessionLogin(
                url,
                userSession.deviceId,
                userSession.userId)
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
                viewModel.getOvoTopUpUrl(getRedirectUrl(), getCustomerData())
            }
            globalError.visible()
            webView.gone()
            progressBar.gone()
        }
    }

    inner class OvoTopUpWebViewClient : WebViewClient() {

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            binding?.progressBar?.gone()
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // Check if url is redirect_url
            if (url == BACK_APPLINK || url == getRedirectUrl()) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            binding?.progressBar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding?.progressBar?.gone()
        }
    }

    companion object {
        private const val BACK_APPLINK = "tokopedia://back"

        private const val QUERY_IS_HIDE_DIGITAL = "is_hide_digital"

        const val EXTRA_REDIRECT_URL = "redirect_url"
        const val EXTRA_IS_HIDE_DIGITAL = "is_hide_digital"
        const val EXTRA_CUSTOMER_DATA = "customer_data"

        fun createInstance(redirectUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData): OvoTopUpWebViewFragment {
            return OvoTopUpWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_REDIRECT_URL, redirectUrl)
                    putInt(EXTRA_IS_HIDE_DIGITAL, isHideDigital)
                    putParcelable(EXTRA_CUSTOMER_DATA, customerData)
                }
            }
        }
    }
}