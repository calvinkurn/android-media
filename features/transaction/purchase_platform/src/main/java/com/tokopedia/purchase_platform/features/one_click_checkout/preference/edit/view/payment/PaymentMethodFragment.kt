package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.payment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary.PreferenceSummaryFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.summary.PreferenceSummaryViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_payment_method.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class PaymentMethodFragment : BaseDaggerFragment() {

    companion object {

        const val FORCE_TIMEOUT = 90000L

        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): PaymentMethodFragment {
            val paymentMethodFragment = PaymentMethodFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            paymentMethodFragment.arguments = bundle
            return paymentMethodFragment
        }
    }

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
        if (parent is PreferenceEditActivity) {
            parent.hideAddButton()
            parent.hideDeleteButton()
            parent.setHeaderTitle("Pilih Metode Pembayaran")
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.hideStepper()
            } else {
                parent.setHeaderSubtitle("Langkah 3 dari 3")
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

//        if (GlobalConfig.isAllowDebuggingTools() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WebView.setWebContentsDebuggingEnabled(true)
//        }
//        web_view.loadUrl("tokopedia://dummy/payment/listing?callback_url=http%3A%2F%2Flocalhost%3A8080%2Fdummy%2Fpayment%2Flisting&customer_email=davin.kurniawan%40tokopedia.com&customer_msisdn=&customer_name=Davin+Kurniawan&description=Manual+Transfer+%28qweqewqe+-+123123123%29&express_checkout_param=%7B%22account_name%22%3A%22qweqewqe%22%2C%22account_number%22%3A%22123123123%22%2C%22bank_id%22%3A%221%22%7D&express_checkout_url=http%3A%2F%2Flocalhost%3A8080%2Fv2%2Fapi%2Fpayment%2FMANUALTRANSFER&gateway_code=MANUALTRANSFER&image=https%3A%2F%2Fecs7.tokopedia.net%2Fimg%2Ftoppay%2Fpayment-logo%2Ficon-bca.png&merchant_code=tokopediatest&message=Success&profile_code=EXPRESS_SAVE&signature=610d56a2c6d12cc17250145b05aeb445b6c806df&success=true&user_id=34437")
        val data = "merchant_code=tokopediatest&profile_code=EXPRESS_SAVE&user_id=${UserSession(context).userId}&customer_name=${UserSession(context).name}&customer_email=${UserSession(context).email}&callback_url=https%3A%2F%2Fpay.tokopedia.com%2Fv2%2Fpayment%2Fregister%2Flisting"
        web_view.postUrl("https://pay-staging.tokopedia.com/v2/payment/register/listing", data.toByteArray())
    }

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
    }

    override fun onStart() {
        super.onStart()
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.setStepperValue(75, true)
        }
    }

    inner class PaymentMethodWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress_bar?.visible()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            progress_bar?.gone()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            val uri = Uri.parse(url)
            val query = uri.query
            val isSuccess = uri.getQueryParameter("success")
            if (isSuccess != null && isSuccess.equals("true", true)) {
                val gatewayCode = uri.getQueryParameter("gateway_code")
                if (gatewayCode != null) {
                    goToNextStep(gatewayCode)
                }
            }
            return super.shouldInterceptRequest(view, url)
        }
    }

    private fun timerObservable(view: WebView) {
        compositeSubscription.add(
                Observable.timer(FORCE_TIMEOUT, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<Long>() {
                            override fun onCompleted() {}
                            override fun onError(e: Throwable) {
                                e.printStackTrace()
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
//        view.stopLoading()
//        showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
    }

    private fun goToNextStep(gatewayCode: String) {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.gatewayCode = gatewayCode
            if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
                parent.onBackPressed()
            } else {
                parent.addFragment(PreferenceSummaryFragment.newInstance())
            }
        }
    }

}
