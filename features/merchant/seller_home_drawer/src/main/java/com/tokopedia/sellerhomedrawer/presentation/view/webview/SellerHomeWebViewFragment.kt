package com.tokopedia.sellerhomedrawer.presentation.view.webview

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.sellerhomedrawer.presentation.view.webview.SellerHomeWebViewActivity.Companion.PARAM_BUNDLE_URL
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class SellerHomeWebViewFragment : BaseWebViewFragment() {

    companion object {
        @JvmStatic
        fun newInstance(url: String?): SellerHomeWebViewFragment {
            val sellerHomeWebViewFragment = SellerHomeWebViewFragment()
            val bundle = Bundle().apply {
                putString(PARAM_BUNDLE_URL, url)
            }
            sellerHomeWebViewFragment.arguments = bundle
            return sellerHomeWebViewFragment
        }
    }

    var urlString: String? = ""
    private val userSession: UserSessionInterface = UserSession(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            urlString = arguments?.getString(PARAM_BUNDLE_URL)
    }

    override fun getAccessToken(): String? = userSession.accessToken

    override fun getUrl(): String {
        return URLGenerator.generateURLSessionLogin(
                urlString,
                userSession.deviceId,
                userSession.userId)
    }

    override fun getUserIdForHeader(): String? = userSession.userId

    override fun loadWeb() {
        super.loadWeb()
        webView.settings.builtInZoomControls = false
    }

}