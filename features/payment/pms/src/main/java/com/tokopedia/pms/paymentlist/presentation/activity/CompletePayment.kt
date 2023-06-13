package com.tokopedia.pms.paymentlist.presentation.activity

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.databinding.ActivityCompletePaymentBinding
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompletePayment : AppCompatActivity() {

    private lateinit var binding: ActivityCompletePaymentBinding
    private var urlToLoad = ""
    private lateinit var timerJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionViewColor()
        initBundleData()
        initView()
        initUrlLoad()
    }

    private fun setActionViewColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor =
                ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        }
    }

    private fun initBundleData() {
        intent?.extras?.let {
            urlToLoad = it.getString(COMPLETE_PAYMENT_URL_KEY, "")
        }
    }

    private fun initUrlLoad() {
        if (urlToLoad.isNotEmpty())
            binding.scroogeExtendedWebview.loadUrl(urlToLoad)
    }

    private fun initView() {
        binding = ActivityCompletePaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.pmsWebviewTitle.text =
            getString(com.tokopedia.pms.R.string.pms_complete_payment_title)
        binding.progressbar.isIndeterminate = true
        binding.btnBack.setOnClickListener {
            webViewBackLogic()
        }
        setWebViewProperties()
    }

    private fun setWebViewProperties() {
        binding.scroogeExtendedWebview.webViewClient = CompletePaymentPmsWebClient()
        binding.scroogeExtendedWebview.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            builtInZoomControls = false
            displayZoomControls = true
        }
    }

    private fun webViewBackLogic() {
        if (binding.scroogeExtendedWebview.canGoBack()) {
            binding.scroogeExtendedWebview.goBack()
        } else
            finish()
    }

    private fun routeToSuccess(appLink: String) {
        RouteManager.route(this, appLink)
        finish()
    }


    inner class CompletePaymentPmsWebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().contains("tokopedia://payment/thankyou?payment_id=")) {
                routeToSuccess(request?.url.toString())
                return true
            }
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            hideProgressBar()
            if (::timerJob.isInitialized)
                timerJob.cancel()
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showProgressBar()
            startTimer()
            super.onPageStarted(view, url, favicon)
        }
    }

    override fun onPause() {
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
        super.onPause()
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            delay(90000)
            binding.scroogeExtendedWebview.stopLoading()
            Toaster.build(
                binding.activityCompletepayment,
                resources.getString(com.tokopedia.pms.R.string.pms_complete_payment_timeout),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
            finish()
        }
    }

    fun showProgressBar() {
        binding.progressbar.visible()
    }

    fun hideProgressBar() {
        binding.progressbar.gone()
    }

    companion object {
        const val COMPLETE_PAYMENT_URL_KEY = "completePaymentUrl"
    }

    override fun onBackPressed() {
        webViewBackLogic()
    }
}
