package com.tokopedia.home_account.linkaccount.view

import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.webview.BaseSessionWebViewFragment

/**
 * Created by Yoris on 27/08/21.
 */

class LinkAccountWebviewFragment: BaseSessionWebViewFragment() {

    override fun shouldOverrideUrlLoading(webview: WebView?, url: String): Boolean {
        when {
            url.contains(GOJEK_PAGE) -> {
                // Check gojek accounts page, show toolbar
                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).showToolbar()
            }
            url.contains(GOPAY_PAGE) -> {
                // Check gopay input pin page, and hide back btn
                (activity as LinkAccountWebViewActivity).showToolbar()
                (activity as LinkAccountWebViewActivity).hideToolbarBackBtn()
                (activity as LinkAccountWebViewActivity).showSkipBtnToolbar()
            }
            url.contains(BACK_BTN_APPLINK) -> {
                // Finish activity from webview
                activity?.finish()
            }
            else -> {
                // Default is hidden
                (activity as LinkAccountWebViewActivity).hideSkipBtnToolbar()
                (activity as LinkAccountWebViewActivity).hideToolbar()
            }
        }
        return super.shouldOverrideUrlLoading(webview, url)
    }

    fun showSkipDialog() {
        if(activity!=null){
            DialogUnify(requireActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Aktifin GoPay lain kali?")
                setDescription("Tenang aja, akun Tokopedia & Gojek kamu tetap kesambung. Kamu bisa aktifin GoPay belakangan.")
                setPrimaryCTAText("Iya, Nanti Aja")
                setSecondaryCTAText("Kembali")
                setPrimaryCTAClickListener {
                    webView?.loadUrl(LinkAccountFragment.getSuccessPage())
                    dismiss()
                }
                setSecondaryCTAClickListener { dismiss() }
            }.show()
        }
    }

    companion object {
        const val KEY_URL = "url"

        const val GOPAY_PAGE = "gws-app.gopayapi.com/app"
        const val GOJEK_PAGE = "accounts.gojek.com"

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