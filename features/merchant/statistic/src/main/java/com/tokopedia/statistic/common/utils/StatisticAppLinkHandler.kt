package com.tokopedia.statistic.common.utils

import android.content.Intent

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object StatisticAppLinkHandler {

    private const val QUERY_PARAM_PAGE = "page"
    private const val QUERY_PARAM_WIDGET = "widget"
    private const val QUERY_PARAM_SORT = "sort"

    /**
     * handle incoming applink to get it's value parameters
     * applink sample : tokopedia://gold-merchant-statistic-dashboard?page=shop-insight&widget=section-187
     * */
    fun handleAppLink(intent: Intent?, callback: (page: String, widget: String, sort: String) -> Unit) {
        val uri = intent?.data
        val page = uri?.getQueryParameter(QUERY_PARAM_PAGE).orEmpty()
        val widget = uri?.getQueryParameter(QUERY_PARAM_WIDGET).orEmpty()
        val sort = uri?.getQueryParameter(QUERY_PARAM_SORT).orEmpty()
        callback(page, widget, sort)
    }
}
