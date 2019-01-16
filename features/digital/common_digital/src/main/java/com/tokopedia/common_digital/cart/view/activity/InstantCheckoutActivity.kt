package com.tokopedia.common_digital.cart.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.webview.TkpdWebView
import com.tokopedia.common_digital.R
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData
import com.tokopedia.common_digital.common.DigitalRouter
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent
import com.tokopedia.network.constant.ErrorNetMessage

import javax.inject.Inject

/**
 * Created by Rizky on 30/08/18.
 */
class InstantCheckoutActivity : BaseSimpleActivity() {

    private var instantCheckoutData: InstantCheckoutData? = null

    @Inject
    lateinit var digitalRouter: DigitalRouter

    internal lateinit var webView: TkpdWebView
    internal var progressBar: ProgressBar? = null

    private val webViewOnKeyListener: View.OnKeyListener
        get() = View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> {
                        this@InstantCheckoutActivity.finish()
                        return@OnKeyListener true
                    }
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        instantCheckoutData = intent.getParcelableExtra(EXTRA_INSTANT_CHECKOUT_DATA)

        super.onCreate(savedInstanceState)

        initInjector()
    }

    private fun initInjector() {
        val digitalComponent = DaggerDigitalCommonComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
        digitalComponent.inject(this)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_instant_checkout
    }

    override fun setupLayout(savedInstanceState: Bundle) {
        super.setupLayout(savedInstanceState)

        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progress_bar)

        progressBar!!.isIndeterminate = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = true
        webView.settings.setAppCacheEnabled(true)
        webView.webViewClient = InstantCheckoutWebViewClient()
        webView.webChromeClient = InstantCheckoutWebViewChromeClient()
        webView.setOnKeyListener(webViewOnKeyListener)

        webView.loadUrl(instantCheckoutData!!.redirectUrl)
    }


    override fun getNewFragment(): Fragment? {
        return null
    }

    private inner class InstantCheckoutWebViewChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress == 100) {
                if (progressBar != null) progressBar!!.visibility = View.GONE
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {

        }

        override fun onConsoleMessage(cm: ConsoleMessage): Boolean {

            return true
        }
    }

    private inner class InstantCheckoutWebViewClient : WebViewClient() {

        private var timeout = true

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (!instantCheckoutData!!.failedCallbackUrl!!.isEmpty() && url.contains(instantCheckoutData!!.failedCallbackUrl!!) || !instantCheckoutData!!.successCallbackUrl!!.isEmpty() && url.contains(instantCheckoutData!!.successCallbackUrl!!)) {
                view.stopLoading()
                finish()
                return true
            } else {
                if (digitalRouter != null
                        && digitalRouter!!.isSupportApplink(url)
                        && digitalRouter!!.intentDeepLinkHandlerActivity != null) {
                    val intent = digitalRouter!!.intentDeepLinkHandlerActivity
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    finish()
                    return true
                } else if (digitalRouter != null) {
                    val urlFinal = digitalRouter!!.getGeneratedOverrideRedirectUrlPayment(url)
                            ?: return super.shouldOverrideUrlLoading(view, url)
                    view.loadUrl(
                            urlFinal,
                            digitalRouter!!.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal)
                    )
                    return true
                } else {
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
        }

        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            timeout = false
            if (progressBar != null) progressBar!!.visibility = View.GONE
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            handler.cancel()
            if (progressBar != null) progressBar!!.visibility = View.GONE
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest,
                                     error: WebResourceError) {
            super.onReceivedError(view, request, error)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            //    Log.d(TAG, "start url instant instantCheckout = " + url);
            Thread(Runnable {
                try {
                    Thread.sleep(FORCE_TIMEOUT)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                if (timeout) {
                    runOnUiThread { showError(view, WebViewClient.ERROR_TIMEOUT) }
                }
            }).start()
            if (progressBar != null) progressBar!!.visibility = View.VISIBLE
        }

        private fun showError(view: WebView, errorCode: Int) {
            val message: String
            when (errorCode) {
                WebViewClient.ERROR_TIMEOUT -> message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                else -> message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT
            }
            view.stopLoading()
            showToastMessageWithForceCloseView(message)
        }
    }

    private fun showToastMessageWithForceCloseView(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {

        val REQUEST_CODE = 2001
        private val FORCE_TIMEOUT = 60000L
        private val TAG = InstantCheckoutActivity::class.java.simpleName

        private val SEAMLESS = "seamless"

        val EXTRA_INSTANT_CHECKOUT_DATA = "EXTRA_INSTANT_CHECKOUT_DATA"

        fun newInstance(context: Context, instantCheckoutData: InstantCheckoutData): Intent {
            return Intent(context, InstantCheckoutActivity::class.java)
                    .putExtra(EXTRA_INSTANT_CHECKOUT_DATA, instantCheckoutData)
        }
    }

}
