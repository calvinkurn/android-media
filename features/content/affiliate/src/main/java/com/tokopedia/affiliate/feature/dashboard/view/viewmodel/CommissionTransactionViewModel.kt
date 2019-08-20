package com.tokopedia.affiliate.feature.dashboard.view.viewmodel


/**
 * @author by yoasfs on 2019-08-12
 */

data class CommissionTransactionViewModel (
        val hasNext : Boolean = false,
        val nextCursor : String = "",
        val historyList: MutableList<CommissionDetailItemViewModel> = ArrayList()
)