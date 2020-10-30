package com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.TkpdWebView
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class OvoTopUpWebViewFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: OvoTopUpWebViewViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OvoTopUpWebViewViewModel::class.java]
    }

    private var webView: TkpdWebView? = null
    private var progressBar: LoaderUnify? = null
    private var globalError: GlobalError? = null

    private var redirectUrl: String? = null

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    private fun getRedirectUrl(): String {
        if (redirectUrl == null) {
            redirectUrl = arguments?.getString(EXTRA_REDIRECT_URL, "") ?: ""
        }
        return redirectUrl!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initWebView()

        observeOvoTopUpUrl()

        // DEBUG
        viewModel.getOvoTopUpUrl(getRedirectUrl())
    }

    private fun initViews(view: View) {
        webView = view.findViewById(R.id.web_view)
        progressBar = view.findViewById(R.id.progress_bar)
        globalError = view.findViewById(R.id.global_error)

        // DEBUG
//        progressBar?.setOnClickListener {
//            viewModel.getOvoTopUpUrl(getRedirectUrl())
//        }
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
        webView?.webViewClient = OvoTopUpWebViewClient()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings?.mediaPlaybackRequiresUserGesture = false
        }
        // DEBUG
//        if (GlobalConfig.isAllowDebuggingTools() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true)
//            webView?.loadUrl("https://www.google.com")
//        }
    }

    private fun observeOvoTopUpUrl() {
        viewModel.ovoTopUpUrl.observe(viewLifecycleOwner, Observer {
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
    }

    private fun loadWebView(url: String) {
        webView?.loadAuthUrl(url, userSession)
        webView?.visible()
        globalError?.gone()
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
            viewModel.getOvoTopUpUrl(getRedirectUrl())
        }
        globalError?.visible()
        webView?.gone()
        progressBar?.gone()
    }

    inner class OvoTopUpWebViewClient : WebViewClient() {

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
            // DEBUG
            progressBar?.gone()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            // Check if url is redirect_url
            if (url == getRedirectUrl()) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
            return super.shouldInterceptRequest(view, url)
        }
    }

    companion object {
        const val EXTRA_REDIRECT_URL = "redirect_url"

        fun createInstance(redirectUrl: String): OvoTopUpWebViewFragment {
            return OvoTopUpWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_REDIRECT_URL, redirectUrl)
                }
            }
        }
    }
}