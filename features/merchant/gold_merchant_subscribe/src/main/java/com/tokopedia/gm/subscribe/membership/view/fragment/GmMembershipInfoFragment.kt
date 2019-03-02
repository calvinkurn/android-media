package com.tokopedia.gm.subscribe.membership.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class GmMembershipInfoFragment : BaseWebViewFragment(){

    private val url = "https://www.tokopedia.com/bantuan/penjual/fitur-jualan/gold-merchant/#apa-itu-gold-merchant"
    private lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(activity)
    }

    override fun getUrl(): String {
        return URLGenerator.generateURLSessionLogin(url, userSession.deviceId, userSession.userId)
    }

    override fun getUserIdForHeader(): String? {
        return userSession.userId
    }

    override fun getAccessToken(): String? {
        return userSession.accessToken
    }


    companion object {
        fun newInstance() = GmMembershipInfoFragment()
    }
}