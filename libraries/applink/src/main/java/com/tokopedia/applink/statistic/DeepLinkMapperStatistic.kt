package com.tokopedia.applink.statistic

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object DeepLinkMapperStatistic {

    private const val QUERY_PARAM_PAGE = "page"
    private const val QUERY_PARAM_DATA_KEY = "data_key"

    fun getStatisticAppLink(uri: Uri): String {
        val page = uri.getQueryParameter(QUERY_PARAM_PAGE).orEmpty()
        val dataKey = uri.getQueryParameter(QUERY_PARAM_DATA_KEY).orEmpty()
        val internalAppLinkStatistic = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
        if (page.isNotBlank()) {
            val param = mapOf<String, Any>(
                    QUERY_PARAM_PAGE to page,
                    QUERY_PARAM_DATA_KEY to dataKey
            )
            return UriUtil.buildUriAppendParams(internalAppLinkStatistic, param)
        }
        return internalAppLinkStatistic
    }
}