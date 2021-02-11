package com.tokopedia.statistic.common.utils

import android.content.Intent

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object StatisticAppLinkHandler {

    private const val QUERY_PARAM_PAGE = "page"

    fun handleAppLink(intent: Intent?, callback: (page: String) -> Unit) {
        val page = intent?.data?.getQueryParameter(QUERY_PARAM_PAGE).orEmpty()
        callback(page)
    }
}