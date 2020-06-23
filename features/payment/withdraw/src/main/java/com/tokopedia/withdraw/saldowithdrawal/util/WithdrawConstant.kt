package com.tokopedia.withdraw.saldowithdrawal.util

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.network.utils.URLGenerator
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.webview.KEY_NEED_LOGIN

object WithdrawConstant {
    private val WEB_DOMAIN_URL = TokopediaUrl.getInstance().WEB

    val SALDO_LOCK_PAY_NOW_URL = WEB_DOMAIN_URL + "fm/modal-toko/dashboard/pembayaran"
    const val WEB_TNC_URL = "https://m.tokopedia.com/terms/withdrawal-sla"

    const val MAX_WITHDRAWAL_INPUT_LENGTH = 14

    private const val APP_LINK_URL_FORMAT = "%s?url=%s"


    private val rekeningPageURL = when (TokopediaUrl.getInstance().TYPE) {
        Env.STAGING -> "https://staging.tokopedia.com/payment/rekening-premium"
        else -> "https://tokopedia.com/payment/rekening-premium"
    }

    fun openRekeningAccountInfoPage(context: Context?) {
        context?.let {
            openSessionBaseURL(context, rekeningPageURL)
        }
    }

    fun openSessionBaseURL(context: Context?, url: String) {
        context?.let {
            val intent = RouteManager.getIntent(context, String.format(APP_LINK_URL_FORMAT,
                    ApplinkConst.WEBVIEW, url)).apply {
                putExtra(KEY_NEED_LOGIN, true)
            }
            context.startActivity(intent)
        }
    }


}