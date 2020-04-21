package com.tokopedia.topads.dashboard.view.fragment

import android.net.Uri
import android.os.Bundle
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.webview.BaseWebViewFragment

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

    override fun getScreenName(): String? = null

    companion object {
        fun createInstance(): TopAdsPaymentCreditFragment = TopAdsPaymentCreditFragment()
    }
}