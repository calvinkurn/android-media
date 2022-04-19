package com.tokopedia.loginregister.termprivacy.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION

/**
 * @author rival
 * @team @minion-kevin
 *
 * @created at 8/09/2020
 *
 * @appLink : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.TERM_PRIVACY]
 * parameter :
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION]
 * [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY]
 * @example :
 * RouteManager.getIntent(getActivity(), TERM_PRIVACY, PAGE_TERM_AND_CONDITION)
 * */

class TermPrivacyActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPage()
    }

    private fun loadPage() {
        intent.data?.let {
            if (it.scheme == DeeplinkConstant.SCHEME_INTERNAL && it.pathSegments.size > 0) {
                when(it.lastPathSegment) {
                    PAGE_TERM_AND_CONDITION -> openTermPage()
                    PAGE_PRIVACY_POLICY -> openPrivacyPage()
                    else -> finish()
                }
            }
        }
    }

    private fun openTermPage() {
        RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TERM))
        finish()
    }

    private fun openPrivacyPage() {
        RouteManager.route(this, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_PRIVACY))
        finish()
    }

    companion object {
        private const val URL_TERM = "https://m.tokopedia.com/terms.pl?device=android&flag_app=1"
        private const val URL_PRIVACY = "https://m.tokopedia.com/privacy.pl?device=android&flag_app=1"
    }
}