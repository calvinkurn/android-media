package com.tokopedia.topads.dashboard.view.fragment

import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.topads.dashboard.TopAdsDashboardRouter
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class TopAdsPaymentCreditFragment : BaseWebViewFragment() {

    private var dataCredit: DataCredit? = null
    private var userSession: UserSessionInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            dataCredit = intent.getParcelableExtra(TopAdsDashboardConstant.EXTRA_CREDIT)
            userSession = UserSession(activity)
        }
    }

    override fun getUrl(): String {
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(dataCredit?.productUrl),
                userSession?.deviceId,
                userSession?.userId)
    }

    override fun getUserIdForHeader(): String? = userSession?.userId

    override fun getAccessToken(): String? = userSession?.accessToken

    override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
        activity?.run {
            if ((application as TopAdsDashboardRouter).isSupportedDelegateDeepLink(url)){
                (application as TopAdsDashboardRouter).actionNavigateByApplinksUrl(this, url, Bundle())
                return true
            }
        }
        return false
    }

    override fun getScreenName(): String? = null

    companion object {
        fun createInstance(): TopAdsPaymentCreditFragment = TopAdsPaymentCreditFragment()
    }
}