package com.tokopedia.home_account.linkaccount.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.linkaccount.di.LinkAccountComponent
import com.tokopedia.home_account.linkaccount.tracker.LinkAccountTracker
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

/**
 * Created by Yoris on 27/08/21.
 */

class LinkAccountWebviewFragment: BaseSessionWebViewFragment() {

    @Inject lateinit var analytics: LinkAccountTracker

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onLoadFinished() {
        super.onLoadFinished()
        checkPageFinished()
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        when {
            url.contains(BACK_BTN_APPLINK, ignoreCase = true) -> {
                // Finish activity from webview
                return super.shouldOverrideUrlLoading(webview, BACK_BTN_APPLINK)
            }
            url.contains(GOJEK_LINK, ignoreCase = true) -> {
                // Check for gojek.link url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                activity?.finish()
                return true
            }
            else -> {
                // Default is hidden
                hideToolbar()
            }
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    fun hideToolbar() {
        (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
        (activity as LinkAccountWebViewActivity).hideToolbar()
    }

    fun checkPageFinished() {
        val mUrl = getWebView().url ?: ""
        when {
            mUrl.contains(TokopediaUrl.Companion.getInstance().GOJEK_OTP, ignoreCase = true) -> {
                // Check gojek accounts page, show toolbar
                analytics.trackViewGojekOTP()
                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).showToolbar()
                (activity as LinkAccountWebViewActivity).setToolbarTitle(getString(R.string.label_toolbar_verifikasi_akun))
            }
            mUrl.contains(TokopediaUrl.Companion.getInstance().GOPAY_PIN, ignoreCase = true) -> {
                analytics.trackViewGopayPin()
                // Check gopay input pin page, and hide back btn
                (activity as LinkAccountWebViewActivity).showToolbar()
                (activity as LinkAccountWebViewActivity).hideToolbarBackBtn()
                (activity as LinkAccountWebViewActivity).showSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).setToolbarTitle(getString(R.string.label_toolbar_aktifasi_gopay))
            }
            else -> { hideToolbar() }
        }
    }

    fun showSkipDialog() {
        if(activity != null){
            analytics.trackClickLewatinToolbar()
            DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.title_skip_gopay_dialog))
                setDescription(getString(R.string.desc_skip_gopay_dialog))
                setPrimaryCTAText(getString(R.string.label_primary_btn_gopay_dialog))
                setSecondaryCTAText(getString(R.string.label_secondary_btn_gopay_dialog))
                setPrimaryCTAClickListener {
                    analytics.trackSkipPopupYes()
                    val baseUrl = LinkAccountWebViewActivity.getLinkAccountUrl(
                        ApplinkConstInternalGlobal.NEW_HOME_ACCOUNT)
                    if(baseUrl != null) {
                        webView?.loadUrl(
                            LinkAccountWebViewActivity.getSuccessUrl(baseUrl).toString()
                        )
                    }
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    analytics.trackSkipPopupNo()
                    dismiss()
                }
            }.show()
        }
    }

    fun trackClickBackBtn() {
        if(getWebView().url?.contains(TokopediaUrl.Companion.getInstance().GOJEK_OTP) == true) {
            analytics.trackClickBtnBack()
        }
    }

    companion object {
        const val KEY_URL = "url"
        const val GOJEK_LINK = "https://gojek.link"
        const val BACK_BTN_APPLINK = "tokopedia://back"

        fun newInstance(url: String): BaseSessionWebViewFragment {
            val fragment = LinkAccountWebviewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }
}