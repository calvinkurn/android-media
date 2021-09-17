package com.tokopedia.home_account.linkaccount.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.home_account.R
import com.tokopedia.webview.BaseSessionWebViewFragment

/**
 * Created by Yoris on 27/08/21.
 */

class LinkAccountWebviewFragment: BaseSessionWebViewFragment() {

    override fun onLoadFinished() {
        when {
            url.contains(GOJEK_OTP_PAGE, ignoreCase = true) -> {
                // Check gojek accounts page, show toolbar
                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).showToolbar()
                (activity as LinkAccountWebViewActivity).setToolbarTitle("Verifikasi Akun")
            }
            url.contains(GOPAY_PIN_PAGE, ignoreCase = true) -> {
                // Check gopay input pin page, and hide back btn
                (activity as LinkAccountWebViewActivity).showToolbar()
                (activity as LinkAccountWebViewActivity).hideToolbarBackBtn()
                (activity as LinkAccountWebViewActivity).showSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).setToolbarTitle("   Aktifin GoPay")
            }
            url.contains(BACK_BTN_APPLINK, ignoreCase = true) -> {
                // Finish activity from webview
                activity?.finish()
            }
            url.startsWith(GOJEK_LINK, ignoreCase = true) -> {
                // Check for gojek.link url
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                activity?.finish()
            }
            else -> {
                // Default is hidden
                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).hideToolbar()
            }
        }
        super.onLoadFinished()
    }

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
//        when {
//            url.contains(GOJEK_OTP_PAGE, ignoreCase = true) -> {
//                // Check gojek accounts page, show toolbar
//                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
//                (activity as LinkAccountWebViewActivity).showToolbar()
//                (activity as LinkAccountWebViewActivity).setToolbarTitle("Verifikasi Akun")
//            }
//            url.contains(GOPAY_PIN_PAGE, ignoreCase = true) -> {
//                // Check gopay input pin page, and hide back btn
//                (activity as LinkAccountWebViewActivity).showToolbar()
//                (activity as LinkAccountWebViewActivity).hideToolbarBackBtn()
//                (activity as LinkAccountWebViewActivity).showSkipBtnToolbar()
//                (activity as LinkAccountWebViewActivity).setToolbarTitle("Aktifin GoPay")
//            }
//            url.contains(BACK_BTN_APPLINK, ignoreCase = true) -> {
//                // Finish activity from webview
//                activity?.finish()
//            }
//            url.startsWith(GOJEK_LINK, ignoreCase = true) -> {
//                // Check for gojek.link url
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(intent)
//                activity?.finish()
//            }
//            else -> {
//                // Default is hidden
//                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
//                (activity as LinkAccountWebViewActivity).hideToolbar()
//            }
//        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    fun showSkipDialog() {
        if(activity != null){
            DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.title_skip_gopay_dialog))
                setDescription(getString(R.string.desc_skip_gopay_dialog))
                setPrimaryCTAText(getString(R.string.label_primary_btn_gopay_dialog))
                setSecondaryCTAText(getString(R.string.label_secondary_btn_gopay_dialog))
                setPrimaryCTAClickListener {
                    webView?.loadUrl(GOJEK_OTP_PAGE)

//                    webView?.loadUrl(LinkAccountWebViewActivity.getSuccessUrl(requireContext()))
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    companion object {
        const val KEY_URL = "url"

        const val GOPAY_PIN_PAGE = "gws-app.gopayapi.com/app"
        const val GOJEK_OTP_PAGE = "accounts-integration.gojek.com"

        const val GOJEK_LINK = "https://gojek.link/"

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