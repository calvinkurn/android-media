package com.tokopedia.home_account.privacy_account.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.home_account.privacy_account.di.LinkAccountComponent
import com.tokopedia.home_account.privacy_account.tracker.LinkAccountTracker
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.BaseSessionWebViewFragment
import javax.inject.Inject

/**
 * Created by Yoris on 27/08/21.
 */
@Deprecated("Remove this class after integrating SCP Login to Tokopedia")
class LinkAccountWebviewFragment: BaseSessionWebViewFragment() {

    @Inject lateinit var analytics: LinkAccountTracker

    override fun initInjector() {
        getComponent(LinkAccountComponent::class.java).inject(this)
    }

    override fun onLoadFinished() {
        super.onLoadFinished()
        checkPageFinished()
    }

    private fun checkForStatusQuery(url: String): Boolean {
        return try {
            val status = Uri.parse(url).getQueryParameter(KEY_STATUS) ?: ""
            activity?.setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_STATUS, status)
            })
            activity?.finish()
            return status.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        when {
            url.startsWith(TKPD_SCHEME, ignoreCase = true) || url.startsWith(TKPD_INTERNAL_SCHEME, ignoreCase = true) -> {
                if(url.startsWith(BACK_BTN_APPLINK)) {
                    // Finish activity from webview
                    if (url.contains(KEY_STATUS) && checkForStatusQuery(url)) {
                        return true
                    }
                    return super.shouldOverrideUrlLoading(webview, BACK_BTN_APPLINK)
                } else {
                    activity?.finish()
                    return super.shouldOverrideUrlLoading(webview, url)
                }
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
        getLinkAccountActivity()?.run {
            hideSkipBtnToolbar()
            hideToolbar()
        }
    }

    fun checkPageFinished() {
        val mUrl = getWebView().url ?: ""
        when {
            mUrl.contains(TokopediaUrl.Companion.getInstance().GOJEK_OTP, ignoreCase = true) -> {
                // Check gojek accounts page, show toolbar
                analytics.trackViewGojekOTP()
                getLinkAccountActivity()?.run {
                    hideSkipBtnToolbar()
                    showToolbar()
                    setToolbarTitle(getString(R.string.label_toolbar_verifikasi_akun))
                }
            }
            mUrl.contains(TokopediaUrl.Companion.getInstance().GOPAY_PIN, ignoreCase = true) -> {
                analytics.trackViewGopayPin()
                // Check gopay input pin page, and hide back btn
                getLinkAccountActivity()?.run {
                    showToolbar()
                    hideToolbarBackBtn()
                    showSkipBtnToolbar()
                    setToolbarTitle(getString(R.string.label_toolbar_aktifasi_gopay))
                }
            }
            else -> { hideToolbar() }
        }
    }

    private fun getLinkAccountActivity(): LinkAccountWebViewActivity? {
        return try {
            activity as LinkAccountWebViewActivity?
        } catch (e: Exception) {
            null
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
                    val baseUrl = LinkAccountWebViewActivity.getLinkAccountUrl("")
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
        const val TKPD_SCHEME = "tokopedia://"
        const val TKPD_INTERNAL_SCHEME = "tokopedia-android-internal://"

        const val KEY_STATUS = "status"

        fun newInstance(url: String): BaseSessionWebViewFragment {
            val fragment = LinkAccountWebviewFragment()
            val args = Bundle()
            args.putString(KEY_URL, url)
            fragment.arguments = args
            return fragment
        }
    }
}
