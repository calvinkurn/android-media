package com.tokopedia.statistic.common.utils

import android.content.Intent

/**
 * Created By @ilhamsuaib on 11/02/21
 */

object StatisticAppLinkHandler {

    private const val QUERY_PARAM_PAGE = "page"
    private const val QUERY_PARAM_DATA_KEY = "data_key"

    fun handleAppLink(intent: Intent?, callback: (page: String, dataKey: String) -> Unit) {
        val uri = intent?.data
        val page = uri?.getQueryParameter(QUERY_PARAM_PAGE).orEmpty()
        val dataKey = uri?.getQueryParameter(QUERY_PARAM_DATA_KEY).orEmpty()
        callback(page, dataKey)
    }
}