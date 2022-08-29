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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.databinding.ActivityCompletePaymentBinding


class CompletePayment : AppCompatActivity() {

    private lateinit var binding: ActivityCompletePaymentBinding
    private var urlToLoad = "https://edition.cnn.com"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window?.statusBarColor =
                    ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            } else {
                window?.statusBarColor =
                    ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }
        }

        initView()
        // initBundleData()
        initUrlLoad()


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

        binding.scroogeExtendedWebview.webViewClient = CompletePaymentPmsWebClient()


    }


    inner class CompletePaymentPmsWebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            hideProgressBar()
            super.onPageFinished(view, url)

        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            showProgressBar()
            super.onPageStarted(view, url, favicon)
        }


    }

    fun showProgressBar() {
        binding.progressbar.visible()
    }

    fun hideProgressBar() {
        binding.progressbar.gone()
    }
}
