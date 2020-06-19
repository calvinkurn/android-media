package com.tokopedia.withdraw.saldowithdrawal.constant

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession

object WithdrawConstant {
    private val WEB_DOMAIN_URL = TokopediaUrl.getInstance().WEB

    val SALDO_LOCK_PAY_NOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran"
    const val WEB_TNC_URL = "https://m.tokopedia.com/terms/withdrawal-sla"

    const val MAX_WITHDRAWAL_INPUT_LENGTH = 14

    const val APP_LINK_URL_FORMAT = "%s?url=%s"


    val rekeningPageURL = when (TokopediaUrl.getInstance().TYPE) {
        Env.STAGING -> "https://staging.tokopedia.com/payment/rekening-premium"
        else -> "https://tokopedia.com/payment/rekening-premium"
    }

    fun openRekeningAccountInfoPage(context: Context?, userSession: UserSession) {
        context?.let {
            val resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(rekeningPageURL), userSession.deviceId, userSession.userId)
            RouteManager.route(context, String.format(APP_LINK_URL_FORMAT,
                    ApplinkConst.WEBVIEW, resultGenerateUrl))
        }
    }

    fun openSessionBaseURL(context: Context?, userSession: UserSession, url: String) {
        context?.let {
            val resultGenerateUrl = URLGenerator.generateURLSessionLogin(
                    Uri.encode(url), userSession.deviceId, userSession.userId)
            RouteManager.route(context, String.format(APP_LINK_URL_FORMAT,
                    ApplinkConst.WEBVIEW, resultGenerateUrl))
        }
    }


}