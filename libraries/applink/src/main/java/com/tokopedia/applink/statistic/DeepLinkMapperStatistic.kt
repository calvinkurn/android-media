package com.tokopedia.applink.statistic

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object DeepLinkMapperStatistic {

    private const val QUERY_PARAM_PAGE = "page"

    fun getStatisticAppLink(uri: Uri): String {
        val page = uri.getQueryParameter(QUERY_PARAM_PAGE)
        val internalAppLinkStatistic = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
        if (!page.isNullOrBlank()) {
            val param = mapOf(QUERY_PARAM_PAGE to page)
            return UriUtil.buildUriAppendParam(internalAppLinkStatistic, param)
        }
        return internalAppLinkStatistic
    }
}