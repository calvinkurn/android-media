package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant.Url.PM_FEE_SERVICE
import com.tokopedia.power_merchant.subscribe.databinding.BottomSheetPmFeeServiceBinding

class PMFeeServiceBottomSheet :
    BaseBottomSheet<BottomSheetPmFeeServiceBinding>() {

    companion object {
        private const val TAG = "PMFeeServiceBottomSheet"
        fun createInstance(): PMFeeServiceBottomSheet {
            return PMFeeServiceBottomSheet()
        }
    }

    override fun bind(view: View) = BottomSheetPmFeeServiceBinding.bind(view)

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_fee_service

    override fun setupView() = binding?.run {
        val title =
            context?.getString(com.tokopedia.power_merchant.subscribe.R.string.pm_fee_service_category)
                ?: String.EMPTY
        setTitle(title)
        progressbar.isIndeterminate = true
        swipeRefresh()
        setupWebView()

    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setupWebView(){
        binding?.wvFeeService?.webViewClient = object:WebViewClient(){

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.progressbar?.gone()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                binding?.progressbar?.gone()
            }
        }
        loadUrl()
    }

    private fun loadUrl(){
        binding?.wvFeeService?.loadUrl(PM_FEE_SERVICE)
    }
    private fun swipeRefresh(){
        binding?.sflWebView?.setOnRefreshListener {
            binding?.sflWebView?.isRefreshing = false
            binding?.progressbar?.visible()
            loadUrl()
        }
    }
}