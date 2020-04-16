package com.tokopedia.payment.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.payment.R
import com.tokopedia.payment.fingerprint.di.DaggerFingerprintComponent
import com.tokopedia.payment.fingerprint.di.FingerprintModule
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant
import com.tokopedia.payment.fingerprint.view.FingerPrintDialogPayment
import com.tokopedia.payment.fingerprint.view.FingerPrintDialogPayment.Companion.createInstance
import com.tokopedia.payment.fingerprint.view.FingerPrintDialogPayment.ListenerPayment
import com.tokopedia.payment.fingerprint.view.FingerprintDialogRegister
import com.tokopedia.payment.fingerprint.view.FingerprintDialogRegister.Companion.createInstance
import com.tokopedia.payment.presenter.TopPayContract
import com.tokopedia.payment.presenter.TopPayPresenter
import com.tokopedia.payment.router.IPaymentModuleRouter
import com.tokopedia.payment.utils.Constant
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.webview.WebViewHelper.appendGAClientIdAsQueryParam
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by kris on 3/9/17. Tokopedia
 */
class TopPayActivity : AppCompatActivity(), TopPayContract.View, ListenerPayment, FingerprintDialogRegister.ListenerRegister, FilePickerInterface {

    @Inject
    lateinit var presenter: TopPayPresenter

    private var scroogeWebView: WebView? = null
    private var progressBar: ProgressBar? = null
    override var paymentPassData: PaymentPassData? = null
        private set

    private var paymentModuleRouter: IPaymentModuleRouter? = null
    private var btnBack: View? = null
    private var btnClose: View? = null
    private var tvTitle: TextView? = null
    private var progressDialog: ProgressDialog? = null
    private var fingerPrintDialogPayment: FingerPrintDialogPayment? = null
    private var fingerPrintDialogRegister: FingerprintDialogRegister? = null
    private var isInterceptOtp = true
    private var webChromeWebviewClient: CommonWebViewClient? = null
    private var mJsHciCallbackFuncName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window?.statusBarColor = resources.getColor(R.color.tkpd_status_green_payment_module, null)
            } else {
                window?.statusBarColor = resources.getColor(R.color.tkpd_status_green_payment_module)
            }
        }

        initInjector()
        intent.extras?.let {
            setupBundlePass(it)
        }
        if (application is IPaymentModuleRouter) {
            paymentModuleRouter = application as IPaymentModuleRouter
        }
        initView()
        initVar()
        setViewListener()
        setActionVar()
    }

    private fun initInjector() {
        DaggerFingerprintComponent
                .builder()
                .fingerprintModule(FingerprintModule())
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    private fun setupBundlePass(extras: Bundle) {
        paymentPassData = extras.getParcelable(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA)
    }

    private fun initView() {
        setContentView(R.layout.activity_top_pay_payment_module)
        tvTitle = findViewById(R.id.tv_title)
        btnBack = findViewById(R.id.btn_back)
        btnClose = findViewById(R.id.btn_close)
        scroogeWebView = findViewById(R.id.scrooge_webview)
        progressBar = findViewById(R.id.progressbar)
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage(getString(R.string.title_loading))
        tvTitle?.text = getString(R.string.toppay_title)
    }

    private fun initVar() {
        webChromeWebviewClient = CommonWebViewClient(this, progressBar)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViewListener() {
        progressBar?.isIndeterminate = true
        val webSettings = scroogeWebView?.settings
        webSettings?.apply {
            val userAgent = String.format("%s [%s/%s]", userAgentString, getString(R.string.app_android), GlobalConfig.VERSION_NAME)
            userAgentString = userAgent
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
            setAppCacheEnabled(true)
        }
        scroogeWebView?.apply {
            webViewClient = TopPayWebViewClient()
            webChromeClient = webChromeWebviewClient
            setOnKeyListener(webViewOnKeyListener)
        }
        btnBack?.visibility = View.VISIBLE
        btnBack?.setOnClickListener { onBackPressed() }
        btnClose?.setOnClickListener { callbackPaymentCanceled() }
    }

    private fun setActionVar() {
        presenter.processUriPayment()
        val message = intent.getStringExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_TOASTER_MESSAGE)
        if (!message.isNullOrEmpty()) {
            scroogeWebView?.let {
                Toaster.make(it, message)
            }
        }
    }

    override fun renderWebViewPostUrl(url: String, postData: ByteArray, isGet: Boolean) {
        if (isGet) {
            scroogeWebView?.loadUrl(url)
        } else {
            scroogeWebView?.postUrl(appendGAClientIdAsQueryParam(url, this), postData)
        }
    }

    override fun showToastMessageWithForceCloseView(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        callbackPaymentCanceled()
    }

    fun navigateToActivity(goToIntent: Intent) {
        startActivity(goToIntent)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBackPressed() {
        if (paymentModuleRouter != null && paymentModuleRouter?.baseUrlDomainPayment != null && scroogeWebView?.url != null && scroogeWebView!!.url.contains(paymentModuleRouter!!.baseUrlDomainPayment!!)) {
            scroogeWebView?.loadUrl("javascript:handlePopAndroid();")
        } else if (isEndThanksPage) {
            callbackPaymentSucceed()
        } else {
            callbackPaymentCanceled()
        }
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    fun callbackPaymentCanceled() {
        hideProgressLoading()
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        setResult(PaymentConstant.PAYMENT_CANCELLED, intent)
        finish()
    }

    fun callbackPaymentSucceed() {
        hideProgressLoading()
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        setResult(PaymentConstant.PAYMENT_SUCCESS, intent)
        finish()
    }

    fun callbackPaymentFailed() {
        hideProgressLoading()
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        setResult(PaymentConstant.PAYMENT_FAILED, intent)
        finish()
    }

    override fun onGoToOtpPage(transactionId: String, urlOtp: String) {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogPayment?.dismiss()
        presenter.getPostDataOtp(transactionId, urlOtp)
    }

    override fun onSuccessGetPostDataOTP(postData: String, urlOtp: String) {
        try {
            isInterceptOtp = false
            scroogeWebView?.postUrl(urlOtp, postData.toByteArray(charset(CHARSET_UTF_8)))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    override fun onErrorGetPostDataOtp(e: Throwable?) {
        NetworkErrorHelper.showSnackbar(this, ErrorHandler.getErrorMessage(this, e))
    }

    override fun onPaymentFingerPrint(transactionId: String?, publicKey: String?, date: String?, signature: String?, userId: String?) {
        presenter.paymentFingerPrint(transactionId, publicKey, date, signature, userId)
    }

    override fun onRegisterFingerPrint(transactionId: String?, publicKey: String?, date: String?, signature: String?, userId: String?) {
        presenter.registerFingerPrint(transactionId, publicKey, date, signature, userId)
    }

    private inner class TopPayWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.isNotEmpty() && url.contains(PaymentFingerprintConstant.APP_LINK_FINGERPRINT) &&
                    paymentModuleRouter?.enableFingerprintPayment == true) {
                val uri = Uri.parse(url)
                val transactionId = uri.getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID)
                fingerPrintDialogRegister = createInstance(presenter.userId, transactionId)
                fingerPrintDialogRegister?.apply {
                    setListenerRegister(this@TopPayActivity)
                    context = this@TopPayActivity
                    show(supportFragmentManager, "fingerprintRegister")
                }
                return true
            }

            /*
              HANYA SEMENTARA HARCODE, NANTI PAKAI APPLINK
             */
            if (url.isNotEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_LIVE + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY) ||
                            url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_STAGING + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY))) {
                val deepLinkUrl = (ApplinkConst.WEBVIEW_PARENT_HOME + "?url=" + URLEncoder.encode(url))
                RouteManager.route(this@TopPayActivity, deepLinkUrl)
                return true
            }
            return if (!paymentPassData?.callbackSuccessUrl.isNullOrEmpty() && url.contains(paymentPassData!!.callbackSuccessUrl)) {
                view.stopLoading()
                callbackPaymentSucceed()
                true
            } else if (!paymentPassData?.callbackFailedUrl.isNullOrEmpty() && url.contains(paymentPassData!!.callbackFailedUrl)) {
                view.stopLoading()
                callbackPaymentFailed()
                true
            } else if (url.contains(ACCOUNTS_URL)) {
                view.stopLoading()
                processRedirectUrlContainsAccountsUrl(url)
                true
            } else if (url.contains(LOGIN_URL)) {
                view.stopLoading()
                showToastMessageWithForceCloseView(getString(R.string.toppay_error_login))
                true
            } else if (url.contains(HCI_CAMERA_KTP)) {
                view.stopLoading()
                mJsHciCallbackFuncName = Uri.parse(url).lastPathSegment
                startActivityForResult(RouteManager.getIntent(this@TopPayActivity, ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE), HCI_CAMERA_REQUEST_CODE)
                true
            } else if (url.contains(HCI_CAMERA_SELFIE)) {
                view.stopLoading()
                mJsHciCallbackFuncName = Uri.parse(url).lastPathSegment
                startActivityForResult(RouteManager.getIntent(this@TopPayActivity, ApplinkConst.HOME_CREDIT_SELFIE_WITH_TYPE), HCI_CAMERA_REQUEST_CODE)
                true
            } else {
                if (ApplinkConst.PAYMENT_BACK_TO_DEFAULT.equals(url, ignoreCase = true)) {
                    if (isEndThanksPage) callbackPaymentSucceed() else callbackPaymentCanceled()
                    true
                } else if (RouteManager.isSupportApplink(this@TopPayActivity, url) && !URLUtil.isNetworkUrl(url)) {
                    val intent = RouteManager.getIntent(this@TopPayActivity, url)
                    intent.data = Uri.parse(url)
                    navigateToActivity(intent)
                    true
                } else {
                    if (paymentModuleRouter != null) {
                        val urlFinal = paymentModuleRouter?.getGeneratedOverrideRedirectUrlPayment(url)
                        if (urlFinal == null) {
                            super.shouldOverrideUrlLoading(view, url)
                        } else {
                            view.loadUrl(urlFinal, paymentModuleRouter?.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal))
                            true
                        }
                    } else {
                        super.shouldOverrideUrlLoading(view, url)
                    }
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            if ((request.url.toString().contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA) || request.url.toString().contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_VERITRANS)) &&
                    isInterceptOtp && request.url.getQueryParameter(PaymentFingerprintConstant.ENABLE_FINGERPRINT).equals("true", ignoreCase = true) &&
                    paymentModuleRouter?.enableFingerprintPayment == true) {
                fingerPrintDialogPayment = createInstance(presenter.userId, request.url.toString(),
                        request.url.getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID))
                fingerPrintDialogPayment?.apply {
                    setListenerPayment(this@TopPayActivity)
                    context = this@TopPayActivity
                    show(supportFragmentManager, "fingerprintPayment")
                }
                view.post { view.stopLoading() }
            }
            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView, url: String) {
            presenter.clearTimeoutSubscription()
            progressBar?.apply {
                visibility = View.GONE
            }
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            handler.cancel()
            progressBar?.apply {
                visibility = View.GONE
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            Timber.w("P1#WEBVIEW_ERROR#'%s';error_code=%s;desc='%s'", request.url, error.errorCode, error.description)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            timerObservable(view)
            progressBar?.apply {
                visibility = View.VISIBLE
            }
        }

        private fun timerObservable(view: WebView) {
            presenter.addTimeoutSubscription(
                    Observable.timer(FORCE_TIMEOUT, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Subscriber<Long>() {
                                override fun onCompleted() {
                                    //do nothing
                                }

                                override fun onError(e: Throwable) {
                                    //do nothing
                                }

                                override fun onNext(aLong: Long) {
                                    if (!isUnsubscribed) {
                                        showErrorTimeout(view)
                                    }
                                }
                            })
            )
        }

        private fun showErrorTimeout(view: WebView) {
            view.stopLoading()
            showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
        }
    }

    private fun processRedirectUrlContainsAccountsUrl(url: String) {
        val uriMain = Uri.parse(url)
        val ld = uriMain.getQueryParameter(KEY_QUERY_LD)
        val urlThanks: String = try {
            URLDecoder.decode(ld, CHARSET_UTF_8)
        } catch (e: Exception) {
            ""
        }
        val uri = Uri.parse(urlThanks)
        val paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID)
        if (paymentId != null) callbackPaymentSucceed() else callbackPaymentFailed()
    }

    val isEndThanksPage: Boolean
        get() {
            if (scroogeWebView?.url.isNullOrEmpty()) return false
            for (thanksUrl in THANK_PAGE_URL_LIST) {
                if (scroogeWebView?.url?.contains(thanksUrl) == true) {
                    return true
                }
            }
            return false
        }

    private val webViewOnKeyListener: View.OnKeyListener
        get() = View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                    return@OnKeyListener true
                }
            }
            false
        }

    override fun onPause() {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogRegister?.stopListening()
        super.onPause()
    }

    private fun hideProgressLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun onSuccessRegisterFingerPrint() {
        fingerPrintDialogRegister?.stopListening()
        fingerPrintDialogRegister?.dismiss()
        NetworkErrorHelper.showGreenCloseSnackbar(this, getString(R.string.fingerprint_label_successed_fingerprint))
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun onErrorRegisterFingerPrint() {
        fingerPrintDialogRegister?.onErrorRegisterFingerPrint()
    }

    override fun showErrorRegisterSnackbar() {
        NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.fingerprint_label_failed_fingerprint))
    }

    override fun showProgressDialog() {
        progressDialog?.show()
    }

    override fun onErrorPaymentFingerPrint() {
        fingerPrintDialogPayment?.onErrorNetworkPaymentFingerPrint()
    }

    override fun onSuccessPaymentFingerprint(url: String?, paramEncode: String?) {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogPayment?.dismiss()
        scroogeWebView?.loadUrl(String.format("%1\$s?%2\$s", url, paramEncode))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == CommonWebViewClient.ATTACH_FILE_REQUEST && webChromeWebviewClient != null) {
            webChromeWebviewClient?.onActivityResult(requestCode, resultCode, intent)
        } else if (requestCode == HCI_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imagePath = intent?.getStringExtra(HCI_KTP_IMAGE_PATH)
            val base64 = encodeToBase64(imagePath)
            if (imagePath != null) {
                val jsCallbackBuilder = StringBuilder()
                jsCallbackBuilder.append("javascript:")
                        .append(mJsHciCallbackFuncName)
                        .append("('")
                        .append(imagePath)
                        .append("'")
                        .append(", ")
                        .append("'")
                        .append(base64)
                        .append("')")
                scroogeWebView?.loadUrl(jsCallbackBuilder.toString())
            }
        }
    }

    companion object {
        private const val ACCOUNTS_URL = "accounts.tokopedia.com"
        const val KEY_QUERY_PAYMENT_ID = "id"
        const val KEY_QUERY_LD = "ld"
        const val CHARSET_UTF_8 = "UTF-8"
        private const val LOGIN_URL = "login.pl"
        private const val HCI_CAMERA_KTP = "android-js-call://ktp"
        private const val HCI_CAMERA_SELFIE = "android-js-call://selfie"
        private const val HCI_KTP_IMAGE_PATH = "ktp_image_path"
        private val THANK_PAGE_URL_LIST = arrayOf("thanks", "thank")
        const val HCI_CAMERA_REQUEST_CODE = 978
        const val FORCE_TIMEOUT = 90000L

        @JvmStatic
        fun createInstance(context: Context, paymentPassData: PaymentPassData?): Intent {
            val intent = Intent(context, TopPayActivity::class.java)
            intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
            return intent
        }

        fun encodeToBase64(imagePath: String?): String {
            val bm = BitmapFactory.decodeFile(imagePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 60, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }
    }
}