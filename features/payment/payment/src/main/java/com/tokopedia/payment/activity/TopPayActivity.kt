package com.tokopedia.payment.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.PaymentLoggingClient
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_BACK_BUTTON_APPLINK
import com.tokopedia.common.payment.utils.LINK_ACCOUNT_SOURCE_PAYMENT
import com.tokopedia.common.payment.utils.LinkStatusMatcher
import com.tokopedia.config.GlobalConfig
import com.tokopedia.fingerprint.util.FingerprintConstant
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.authentication.*
import com.tokopedia.network.authentication.AuthKey.Companion.KEY_WSV4
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.network.utils.ThemeUtils
import com.tokopedia.payment.R
import com.tokopedia.payment.fingerprint.di.DaggerFingerprintComponent
import com.tokopedia.payment.fingerprint.di.FingerprintModule
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant
import com.tokopedia.payment.fingerprint.view.FingerPrintDialogPayment
import com.tokopedia.payment.fingerprint.view.FingerprintDialogRegister
import com.tokopedia.payment.presenter.TopPayContract
import com.tokopedia.payment.presenter.TopPayPresenter
import com.tokopedia.payment.utils.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.WebViewHelper
import kotlinx.android.synthetic.main.activity_top_pay_payment_module.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by kris on 3/9/17. Tokopedia
 */
class TopPayActivity :
    AppCompatActivity(),
    TopPayContract.View,
    FingerPrintDialogPayment.ListenerPayment,
    FingerprintDialogRegister.ListenerRegister,
    FilePickerInterface {

    @Inject
    lateinit var presenter: TopPayPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    override var paymentPassData: PaymentPassData? = null
        private set

    private val remoteConfig: RemoteConfig by lazy { FirebaseRemoteConfigImpl(this.applicationContext) }

    private var scroogeWebView: WebView? = null
    private var progressBar: ProgressBar? = null
    private var btnBack: View? = null
    private var tvTitle: TextView? = null
    private var progressDialog: ProgressDialog? = null
    private var mainContainer: RelativeLayout? = null

    private var fingerPrintDialogPayment: FingerPrintDialogPayment? = null
    private var fingerPrintDialogRegister: FingerprintDialogRegister? = null

    private var isInterceptOtp = true
    private var mJsHciCallbackFuncName: String? = null

    private var webChromeWebviewClient: CommonWebViewClient? = null

    // Flag to prevent calling BACK_DIALOG_URL before web view loaded
    private var hasFinishedFirstLoad: Boolean = false

    private var isPaymentPageLoadingTimeout: Boolean = false

    private val paymentPageTimeOutLogging by lazy { PaymentPageTimeOutLogging(this.application) }

    private var reloadUrl = ""

    private val webViewOnKeyListener: View.OnKeyListener
        get() = View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed()
                return@OnKeyListener true
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window?.statusBarColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500, null)
            } else {
                window?.statusBarColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }
        }
        initInjector()
        intent.extras?.let {
            setupBundlePass(it)
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
        try {
            setContentView(R.layout.activity_top_pay_payment_module)
            mainContainer = findViewById(R.id.activity_topay_container)
            tvTitle = findViewById(R.id.tv_title)
            btnBack = findViewById(R.id.btn_back)
            scroogeWebView = findViewById(R.id.scrooge_webview)
            progressBar = findViewById(R.id.progressbar)
            progressDialog = ProgressDialog(this)
            progressDialog?.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
            tvTitle?.text = getString(R.string.toppay_title)
            val currentTransactionId = paymentPassData?.transactionId ?: ""
            tvTitle?.contentDescription = getString(R.string.toppay_title_content_desc, currentTransactionId)
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P1,
                "WEBVIEW_ERROR",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e).take(LOG_TIMEOUT),
                    "data" to ""
                )
            )
            finish()
        }
    }

    private fun initVar() {
        webChromeWebviewClient = if (isPaymentJSLoggingEnabled()) {
            PaymentLoggingClient(this, progressBar)
        } else {
            CommonWebViewClient(this, progressBar)
        }
    }

    private fun isPaymentJSLoggingEnabled(): Boolean {
        return remoteConfig.getBoolean(PaymentConstant.KEY_ENABLE_JS_LOGGIN, false).or(false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setViewListener() {
        progressBar?.isIndeterminate = true

        val webSettings = scroogeWebView?.settings
        webSettings?.apply {
            userAgentString = String.format("%s [%s/%s]", userAgentString, getString(R.string.app_android), GlobalConfig.VERSION_NAME)
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
//            setAppCacheEnabled(true)
        }
        scroogeWebView?.apply {
            webViewClient = TopPayWebViewClient()
            webChromeClient = webChromeWebviewClient
            setOnKeyListener(webViewOnKeyListener)
        }

        btnBack?.visibility = View.VISIBLE
        btnBack?.setOnClickListener { onBackPressed() }
    }

    private fun setActionVar() {
        presenter.processUriPayment()

        val message = intent.getStringExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_TOASTER_MESSAGE)
        if (!message.isNullOrEmpty()) {
            scroogeWebView?.let {
                Toaster.build(it, message).show()
            }
        }
    }

    override fun renderWebViewPostUrl(url: String, postData: ByteArray, isGet: Boolean) {
        if (isGet || isInsufficientBookingStockUrl(url)) {
            scroogeWebView?.loadUrl(url)
        } else {
            scroogeWebView?.postUrl(WebViewHelper.appendGAClientIdAsQueryParam(url, this) ?: "", postData)
        }
    }

    private fun isInsufficientBookingStockUrl(url: String): Boolean {
        val uri = Uri.parse(url)
        val path = uri.path
        return path != null && path.startsWith(INSUFFICIENT_STOCK_URL_PATH, ignoreCase = true)
    }

    override fun showToastMessageWithForceCloseView(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        callbackPaymentCanceled()
    }

    fun navigateAutoReload(goToIntent: Intent) {
        startActivityForResult(goToIntent, REQURST_CODE_AUTO_RELOAD)
    }

    fun navigateToActivityAndFinish(goToIntent: Intent) {
        startActivity(goToIntent)
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun callbackPaymentCanceled() {
        hideProgressLoading()
        var hasClearRedState = false
        intent?.extras?.let {
            hasClearRedState = it.getBoolean(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT)
        }
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        intent.putExtra(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT, hasClearRedState)
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
        var hasClearRedState = false
        intent?.extras?.let {
            hasClearRedState = it.getBoolean(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT)
        }
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        intent.putExtra(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT, hasClearRedState)
        setResult(PaymentConstant.PAYMENT_FAILED, intent)
        finish()
    }

    private fun showFingerprintDialogRegister(url: String) {
        val uri = Uri.parse(url)
        val transactionId = uri.getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID)
        fingerPrintDialogRegister = FingerprintDialogRegister.createInstance(presenter.userId, transactionId)
        fingerPrintDialogRegister?.apply {
            setListenerRegister(this@TopPayActivity)
            context = this@TopPayActivity
            show(supportFragmentManager, "fingerprintRegister")
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
        if (paymentId != null) {
            callbackPaymentSucceed()
        } else {
            callbackPaymentFailed()
        }
    }

    private fun isEndThanksPage(url: String?): Boolean {
        if (url.isNullOrEmpty()) {
            return false
        }
        for (thanksUrl in THANK_PAGE_URL_LIST) {
            if (url.contains(thanksUrl)) {
                return true
            }
        }
        return false
    }

    private fun showProgressLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideProgressLoading() {
        progressBar?.visibility = View.GONE
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun showProgressDialog() {
        if (!isFinishing) {
            progressDialog?.show()
        }
    }

    override fun onGoToOtpPage(transactionId: String, urlOtp: String) {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogPayment?.dismiss()
        presenter.getPostDataOtp(transactionId, urlOtp)
    }

    override fun onSuccessGetPostDataOTP(postData: String, urlOtp: String) {
        try {
            isInterceptOtp = false
            scroogeWebView?.postUrl(urlOtp, postData.toByteArray())
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

    override fun onErrorRegisterFingerPrint() {
        fingerPrintDialogRegister?.onErrorRegisterFingerPrint()
    }

    override fun showErrorRegisterSnackbar() {
        NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.fingerprint_label_failed_fingerprint))
    }

    override fun onSuccessRegisterFingerPrint() {
        fingerPrintDialogRegister?.stopListening()
        fingerPrintDialogRegister?.dismiss()
        NetworkErrorHelper.showGreenCloseSnackbar(this, getString(R.string.fingerprint_label_successed_fingerprint))
    }

    override fun onErrorPaymentFingerPrint() {
        fingerPrintDialogPayment?.onErrorNetworkPaymentFingerPrint()
    }

    override fun onSuccessPaymentFingerprint(url: String?, paramEncode: String?) {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogPayment?.dismiss()
        scroogeWebView?.loadUrl(String.format("%1\$s?%2\$s", url, paramEncode))
    }

    override fun onPause() {
        fingerPrintDialogPayment?.stopListening()
        fingerPrintDialogRegister?.stopListening()
        super.onPause()
    }

    override fun onBackPressed() {
        val url = scroogeWebView?.url
        if (url != null && url.contains(getBaseUrlDomainPayment()) && isHasFinishedFirstLoad()) {
            scroogeWebView?.loadUrl(BACK_DIALOG_URL)
        } else if (isEndThanksPage(url)) {
            callbackPaymentSucceed()
        } else {
            callbackPaymentCanceled()
        }
    }

    private fun isHasFinishedFirstLoad(): Boolean {
        return hasFinishedFirstLoad
    }

    override fun onDestroy() {
        presenter.detachView()
        scroogeWebView = null
        super.onDestroy()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == CommonWebViewClient.ATTACH_FILE_REQUEST && webChromeWebviewClient != null) {
            webChromeWebviewClient?.onActivityResult(requestCode, resultCode, intent)
        } else if (requestCode == REQUEST_CODE_LIVENESS && resultCode == Activity.RESULT_OK) {
            val redirectionUrl = intent?.getStringExtra(ApplinkConstInternalGlobal.PARAM_REDIRECT_URL) ?: ""
            if (redirectionUrl.isNotEmpty()) {
                scroogeWebView?.loadUrl(redirectionUrl)
            }
        } else if (requestCode == HCI_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imagePath = intent?.getStringExtra(HCI_KTP_IMAGE_PATH)
            sendKycImagePathToLite(imagePath)
        } else if (requestCode == REQUEST_CODE_LINK_ACCOUNT) {
            hideProgressDialog()
            if (resultCode == Activity.RESULT_OK) {
                val status = intent?.getStringExtra(ApplinkConstInternalGlobal.PARAM_STATUS) ?: ""
                if (status.isNotEmpty()) {
                    handleStatusMatching(status)
                }
                reloadPayment()
            } else {
                hideFullLoading()
            }
        } else if (requestCode == REQURST_CODE_AUTO_RELOAD) {
            if (reloadUrl.contains(getBaseUrlDomainPayment())) {
                reloadPayment()
            }
        }
    }

    private fun sendKycImagePathToLite(imagePath: String?) {
        if (!imagePath.isNullOrEmpty()) {
            val base64 = encodeToBase64(imagePath)
            base64?.let {
                val jsCallbackBuilder = StringBuilder()
                jsCallbackBuilder.append("javascript:")
                    .append(mJsHciCallbackFuncName)
                    .append("('")
                    .append(imagePath)
                    .append("'")
                    .append(", ")
                    .append("'")
                    .append(it)
                    .append("')")
                scroogeWebView?.loadUrl(jsCallbackBuilder.toString())
            }
        }
    }

    private fun handleStatusMatching(status: String) {
        val message = LinkStatusMatcher.getStatus(status)
        if (message.isNotEmpty()) {
            showToaster(message, Toaster.TYPE_NORMAL)
        }
    }

    private fun showToaster(message: String, type: Int) {
        mainContainer?.run {
            Toaster.build(this, message, Toaster.LENGTH_LONG, type).show()
        }
    }

    private fun reloadPayment() {
        // scroogeWebView?.reload() doesn't work
        scroogeWebView?.loadUrl(reloadUrl)
    }

    private fun encodeToBase64(imagePath: String?): String? {
        return try {
            val bm = BitmapFactory.decodeFile(imagePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_COMPRESS_QUALITY, baos)
            val b = baos.toByteArray()
            Base64.encodeToString(b, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    private fun showFullLoading() {
        scroogeWebView?.visibility = View.INVISIBLE
        showProgressDialog()
    }

    private fun hideFullLoading() {
        scroogeWebView?.visibility = View.VISIBLE
        hideProgressDialog()
    }

    private fun showCreditCardLoader() = activity_topay_container.post {
        loaderCreditCardUnify?.visibility = View.VISIBLE
    }

    private fun hideCreditCardLoader() = activity_topay_container.post {
        loaderCreditCardUnify?.visibility = View.GONE
    }

    private fun routeToHomeCredit(appLink: String, overlayUrl: String?, headerText: String?) {
        val intent = RouteManager.getIntent(this@TopPayActivity, appLink)
        if (!overlayUrl.isNullOrEmpty()) intent.putExtra(CUST_OVERLAY_URL, overlayUrl)
        if (!headerText.isNullOrEmpty()) intent.putExtra(CUST_HEADER, headerText)
        startActivityForResult(intent, HCI_CAMERA_REQUEST_CODE)
    }

    private inner class TopPayWebViewClient : WebViewClient() {

        fun gotoLinkAccount() {
            showFullLoading()
            reloadUrl = scroogeWebView?.url ?: ""
            val intent = RouteManager.getIntent(this@TopPayActivity, ApplinkConstInternalUserPlatform.LINK_ACCOUNT_WEBVIEW)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_LD, LINK_ACCOUNT_BACK_BUTTON_APPLINK)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, LINK_ACCOUNT_SOURCE_PAYMENT)
            startActivityForResult(intent, REQUEST_CODE_LINK_ACCOUNT)
        }

        fun goToAlaCarteKyc(uri: Uri) {
            val projectId = uri.getQueryParameter(ApplinkConstInternalGlobal.PARAM_PROJECT_ID) ?: ""
            val kycRedirectionUrl = uri.getQueryParameter(ApplinkConstInternalGlobal.PARAM_REDIRECT_URL) ?: ""

            val intent = RouteManager.getIntent(this@TopPayActivity, ApplinkConst.KYC_FORM_ONLY, projectId, kycRedirectionUrl)
            startActivityForResult(intent, REQUEST_CODE_LIVENESS)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                val uri = Uri.parse(url)
                if (uri.isOpaque) {
                    return super.shouldOverrideUrlLoading(view, url)
                }
                // fingerprint
                if (url.isNotEmpty() && url.contains(PaymentFingerprintConstant.APP_LINK_FINGERPRINT) &&
                    getEnableFingerprintPayment()
                ) {
                    showFingerprintDialogRegister(url)
                    return true
                }

                if (url.isNotEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_LIVE_HELP_URL) || url.contains(Constant.TempRedirectPayment.TOP_PAY_STAGING_HELP_URL))) {
                    val deepLinkUrl = (ApplinkConstInternalGlobal.WEBVIEW_BACK_HOME + "?url=" + url)
                    RouteManager.route(this@TopPayActivity, deepLinkUrl)
                    return true
                }

                if (url.isNotEmpty() && url.startsWith(ApplinkConst.LINK_ACCOUNT)) {
                    gotoLinkAccount()
                    return true
                }

                // success payment
                val callbackSuccessUrl = paymentPassData?.callbackSuccessUrl
                if (!callbackSuccessUrl.isNullOrEmpty() && url.contains(callbackSuccessUrl)) {
                    view?.stopLoading()
                    callbackPaymentSucceed()
                    return true
                }

                // failed payment
                val callbackFailedUrl = paymentPassData?.callbackFailedUrl
                if (!callbackFailedUrl.isNullOrEmpty() && url.contains(callbackFailedUrl)) {
                    view?.stopLoading()
                    callbackPaymentFailed()
                    return true
                }
                if (url.contains(ACCOUNTS_URL)) {
                    view?.stopLoading()
                    processRedirectUrlContainsAccountsUrl(url)
                    return true
                }
                if (url.contains(LOGIN_URL)) {
                    view?.stopLoading()
                    showToastMessageWithForceCloseView(getString(R.string.toppay_error_login))
                    return true
                }

                val queryParam = uri.getQueryParameter(CUST_OVERLAY_URL)
                val headerText = uri.getQueryParameter(CUST_HEADER)
                // hci
                if (url.contains(HCI_CAMERA_KTP)) {
                    view?.stopLoading()
                    mJsHciCallbackFuncName = Uri.parse(url).lastPathSegment
                    routeToHomeCredit(ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE, queryParam, headerText)
                    return true
                }
                if (url.contains(HCI_CAMERA_SELFIE)) {
                    view?.stopLoading()
                    mJsHciCallbackFuncName = Uri.parse(url).lastPathSegment
                    routeToHomeCredit(ApplinkConst.HOME_CREDIT_SELFIE_WITH_TYPE, queryParam, headerText)
                    return true
                }

                // back
                if (ApplinkConst.PAYMENT_BACK_TO_DEFAULT.equals(url, true)) {
                    if (isEndThanksPage(url)) {
                        callbackPaymentSucceed()
                    } else {
                        callbackPaymentCanceled()
                    }
                    return true
                }

                // applink
                if (RouteManager.isSupportApplink(this@TopPayActivity, url) && !URLUtil.isNetworkUrl(url)) {
                    applinkRedirect(url)
                    return true
                }
                // applink for link aja...
                if (isLinkAjaAppLink(url)) {
                    redirectToLinkAjaApp(url)
                    return true
                }

                val urlFinal = getGeneratedOverrideRedirectUrlPayment(url)

                if (urlFinal.isNotEmpty() && urlFinal.contains(LINK_ATOM_GOPAY)) {
                    view?.loadUrl(urlFinal, getGeneratedOverrideRedirectHeaderUrlPaymentWithoutAuth(urlFinal))
                    return true
                }

                if (urlFinal.isNotEmpty()) {
                    view?.loadUrl(urlFinal, getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal))
                    return true
                }
                if (url.startsWith(ApplinkConst.KYC_FORM_ONLY_NO_PARAM)) {
                    goToAlaCarteKyc(uri)
                    return true
                }
            }

            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            val uri = request?.url
            if (uri != null) {
                val uriString = uri.toString()
                if ((uriString.contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA) || uriString.contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_VERITRANS)) &&
                    isInterceptOtp && uri.getQueryParameter(PaymentFingerprintConstant.ENABLE_FINGERPRINT).equals("true", true) &&
                    getEnableFingerprintPayment()
                ) {
                    fingerPrintDialogPayment = FingerPrintDialogPayment.createInstance(
                        presenter.userId,
                        uriString,
                        uri.getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID)
                    )
                    fingerPrintDialogPayment?.apply {
                        setListenerPayment(this@TopPayActivity)
                        context = this@TopPayActivity
                        show(supportFragmentManager, "fingerprintPayment")
                    }
                    view?.post { view?.stopLoading() }
                }

                // cc loading
                if (uriString.equals(CC_LOADING_URL)) {
                    showCreditCardLoader()
                }

                if (uriString.equals(CC_LOADING_COMPLETE)) {
                    hideCreditCardLoader()
                }
            }

            return super.shouldInterceptRequest(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            logPaymentPageSuccessAfterTimeOut(url)
            hasFinishedFirstLoad = true
            presenter.clearTimeoutSubscription()
            hideProgressLoading()
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)
            handler?.cancel()
            hideProgressLoading()
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            if (isMainPaymentPageTimeOut(
                    request?.url,
                    error?.errorCode ?: 0
                )
            ) {
                handleMainPaymentPageTimeOut(request, error)
            } else {
                ServerLogger.log(
                    Priority.P1,
                    webviewError,
                    mapOf(
                        "type" to request?.url.toString(),
                        "error_code" to error?.errorCode.toString(),
                        "desc" to error?.description?.toString().orEmpty()
                    )
                )
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            view?.let {
                timerObservable(it)
                showProgressLoading()
            }
        }

        private fun timerObservable(view: WebView) {
            presenter.addTimeoutSubscription(
                Observable.timer(FORCE_TIMEOUT, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Long>() {
                        override fun onCompleted() {}

                        override fun onError(e: Throwable) {}

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

    @TargetApi(Build.VERSION_CODES.M)
    private fun handleMainPaymentPageTimeOut(request: WebResourceRequest?, error: WebResourceError?) {
        isPaymentPageLoadingTimeout = true
        paymentPageTimeOutLogging.logCurrentPaymentPageTimeOut(
            request?.url.toString(),
            error?.errorCode.toString(),
            error?.description?.toString().orEmpty()
        )
        closePaymentPageOnTimeOut()
    }

    private fun closePaymentPageOnTimeOut() {
        hideProgressLoading()
        var hasClearRedState = false
        intent?.extras?.let {
            hasClearRedState = it.getBoolean(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT)
        }
        val intent = Intent()
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        intent.putExtra(PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT, hasClearRedState)
        intent.putExtra(PaymentConstant.EXTRA_PAGE_TIME_OUT, true)
        setResult(PaymentConstant.PAYMENT_CANCELLED, intent)
        finish()
    }

    private fun logPaymentPageSuccessAfterTimeOut(url: String?) {
        url?.let {
            if (!isPaymentPageLoadingTimeout &&
                url.toString().startsWith(getBaseUrlDomainPayment() + "/v2/payment")
            ) {
                paymentPageTimeOutLogging.logPaymentPageSuccessAfterTimeOut(url)
            }
        }
    }

    private fun isMainPaymentPageTimeOut(url: Uri?, errorCode: Int): Boolean {
        if (errorCode == WebViewClient.ERROR_TIMEOUT) {
            url?.let {
                return (url.toString().startsWith(getBaseUrlDomainPayment() + "/v2/payment"))
            }
        }
        return false
    }

    private fun getBaseUrlDomainPayment(): String {
        return TokopediaUrl.getInstance().PAY
    }

    fun getGeneratedOverrideRedirectUrlPayment(originUrl: String): String {
        val originUri = Uri.parse(originUrl)
        val uriBuilder = Uri.parse(originUrl).buildUpon()
        if (!originUri.isOpaque) {
            if (!TextUtils.isEmpty(originUri.getQueryParameter(WEBVIEW_FLAG_PARAM_FLAG_APP))) {
                uriBuilder.appendQueryParameter(
                    WEBVIEW_FLAG_PARAM_FLAG_APP,
                    DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP
                )
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(WEBVIEW_FLAG_PARAM_DEVICE))) {
                uriBuilder.appendQueryParameter(
                    WEBVIEW_FLAG_PARAM_DEVICE,
                    DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
                )
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(WEBVIEW_FLAG_PARAM_UTM_SOURCE))) {
                uriBuilder.appendQueryParameter(
                    WEBVIEW_FLAG_PARAM_UTM_SOURCE,
                    DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE
                )
            }
            if (!TextUtils.isEmpty(originUri.getQueryParameter(WEBVIEW_FLAG_PARAM_APP_VERSION))) {
                uriBuilder.appendQueryParameter(
                    WEBVIEW_FLAG_PARAM_APP_VERSION,
                    GlobalConfig.VERSION_NAME
                )
            }
        }
        return uriBuilder.build().toString().trim()
    }

    fun getGeneratedOverrideRedirectHeaderUrlPayment(originUrl: String): Map<String, String> {
        val uri = Uri.parse(originUrl)
        return generateWebviewHeaders(uri.path ?: "", uri.query ?: "")
    }

    fun getGeneratedOverrideRedirectHeaderUrlPaymentWithoutAuth(originUrl: String): MutableMap<String, String> {
        val uri = Uri.parse(originUrl)
        val headerMap = generateWebviewHeaders(uri.path ?: "", uri.query ?: "")
        headerMap.remove(HEADER_AUTHORIZATION)
        return headerMap
    }

    private fun generateWebviewHeaders(path: String, strParam: String): MutableMap<String, String> {
        val header = AuthHelper.getDefaultHeaderMapOld(path, strParam, "GET", CONTENT_TYPE, KEY_WSV4, DATE_FORMAT, userSession.userId, userSession, ThemeUtils.getHeader(this))
        header[HEADER_TKPD_USER_AGENT] = DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
        header[HEADER_TKPD_SESSION_ID] = userSession.deviceId
        return header
    }

    fun getEnableFingerprintPayment(): Boolean {
        return remoteConfig.getBoolean(FingerprintConstant.ENABLE_FINGERPRINT_MAINAPP)
    }

    private fun isLinkAjaAppLink(url: String): Boolean {
        return url.contains(LINK_AJA_APP_LINK)
    }

    private fun isPaymentReloadTrue(decodedURL: String): Boolean {
        return decodedURL.contains(PAYMENT_RELOAD_IS_TRUE)
    }

    private fun isPaymentReloadFalse(decodedURL: String): Boolean {
        return decodedURL.contains(PAYMENT_RELOAD_IS_FALSE)
    }

    private fun redirectToLinkAjaApp(url: String) {
        try {
            val uri = Uri.parse(url)
            val linkAjaIntent = Intent(Intent.ACTION_VIEW, uri)
            val activities = packageManager
                .queryIntentActivities(linkAjaIntent, 0)
            val isIntentSafe: Boolean = activities.isNotEmpty()
            if (isIntentSafe) {
                startActivity(linkAjaIntent)
            }
        } catch (e: ActivityNotFoundException) {
            Timber.e(e)
        }
    }

    private fun applinkRedirect(url: String) {
        val intent = RouteManager.getIntent(this@TopPayActivity, url).apply {
            data = Uri.parse(url)
        }
        val decodedURL = try {
            URLDecoder.decode(url, CHARSET_UTF_8)
        } catch (e: Exception) {
            ""
        }

        if (isPaymentReloadTrue(decodedURL)) {
            reloadUrl = scroogeWebView?.url.orEmpty()
            navigateAutoReload(intent)
        } else if (isPaymentReloadFalse(decodedURL)) {
            startActivity(intent)
        } else {
            navigateToActivityAndFinish(intent)
        }
    }

    companion object {
        const val KEY_QUERY_PAYMENT_ID = "id"
        const val KEY_QUERY_LD = "ld"
        const val CHARSET_UTF_8 = "UTF-8"

        const val HCI_CAMERA_REQUEST_CODE = 978
        private const val REQUEST_CODE_LIVENESS = 1235
        const val FORCE_TIMEOUT = 90000L

        const val LOG_TIMEOUT = 1000
        const val webviewError = "WEBVIEW_ERROR"

        private const val IMAGE_COMPRESS_QUALITY = 60

        private const val LINK_AJA_APP_LINK = "https://linkaja.id/applink/payment"
        private const val LINK_ATOM_GOPAY = "afi.gopaylater.co.id"
        private const val ACCOUNTS_URL = "accounts.tokopedia.com"
        private const val LOGIN_URL = "login.pl"
        private const val HCI_CAMERA_KTP = "android-js-call://ktp"
        private const val HCI_CAMERA_SELFIE = "android-js-call://selfie"
        private const val CC_LOADING_COMPLETE = "https://centinelapi.cardinalcommerce.com/V1/Cruise/CollectRedirect"
        private const val CC_LOADING_URL = "https://centinelapi.cardinalcommerce.com/V1/Cruise/Collect"
        private const val HCI_KTP_IMAGE_PATH = "ktp_image_path"
        private val THANK_PAGE_URL_LIST = arrayOf("thanks", "thank")
        private const val INSUFFICIENT_STOCK_URL_PATH = "/cart/insufficient_booking_stock"
        private val GOPAY_TOP_UP = "${TokopediaUrl.getInstance().WEB}gopay/top-up"

        private const val BACK_DIALOG_URL = "javascript:handlePopAndroid();"
        private const val CUST_OVERLAY_URL = "imgurl"
        private const val CUST_HEADER = "header_text"

        private const val REQUEST_CODE_LINK_ACCOUNT = 101
        private const val REQURST_CODE_AUTO_RELOAD = 103

        private const val PAYMENT_RELOAD_IS_TRUE = "payment_reload=true"
        private const val PAYMENT_RELOAD_IS_FALSE = "payment_reload=false"

        @JvmStatic
        fun createInstance(context: Context, paymentPassData: PaymentPassData?): Intent {
            return Intent(context, TopPayActivity::class.java).apply {
                putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
            }
        }
    }
}
