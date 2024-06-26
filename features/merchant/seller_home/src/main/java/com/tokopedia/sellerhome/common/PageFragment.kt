package com.tokopedia.sellerhome.common

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created By @ilhamsuaib on 2020-03-06
 */

class PageFragment(
    @FragmentType val type: Int,
    val tabPage: String = "",
    val keywordSearch: String = "",
    val orderType: String = "0",
    val orderId: String = "",
    val productManageTab: String = String.EMPTY
)