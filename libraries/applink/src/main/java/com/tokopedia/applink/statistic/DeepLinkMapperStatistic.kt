package com.tokopedia.applink.statistic

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object DeepLinkMapperStatistic {

    private const val QUERY_PARAM_PAGE = "page"
    private const val QUERY_PARAM_WIDGET = "widget"

    /**
     * mapping the external applink to internal applink
     * example :
     * input : tokopedia://gold-merchant-statistic-dashboard?page=shop-insight&widget=pieChart-183
     * output : tokopedia-android-internal://merchant/statistic_dashboard?page=shop-insight&widget=pieChart-183
     * */
    fun getStatisticAppLink(uri: Uri): String {
        val page = uri.getQueryParameter(QUERY_PARAM_PAGE).orEmpty()
        val widget = uri.getQueryParameter(QUERY_PARAM_WIDGET).orEmpty()
        val internalAppLinkStatistic = ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD
        if (page.isNotBlank()) {
            val param = mapOf<String, Any>(
                    QUERY_PARAM_PAGE to page,
                    QUERY_PARAM_WIDGET to widget
            )
            return UriUtil.buildUriAppendParams(internalAppLinkStatistic, param)
        }
        return internalAppLinkStatistic
    }
}