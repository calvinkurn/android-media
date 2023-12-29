package com.tokopedia.sellerorder.filter.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.Utils

object SomFilterUtil {

    private const val DEFAULT_DATE_FILTER_PAST_MONTH = 3

    fun getDefaultDateFilter(format: String = SomConsts.PATTERN_DATE_PARAM): Pair<String, String> {
        return Utils.getNPastMonthTimeText(DEFAULT_DATE_FILTER_PAST_MONTH, format) to Utils.getNPastMonthTimeText(Int.ZERO, format)
    }

    fun getDefaultSortBy(orderStatusFilterKey: String): Long {
        return if (Utils.isEnableOperationalGuideline()) {
            if (orderStatusFilterKey in listOf(
                    SomConsts.STATUS_NEW_ORDER,
                    SomConsts.KEY_CONFIRM_SHIPPING
                )
            ) {
                SomConsts.SORT_BY_DEADLINE_DATE_ASCENDING
            } else {
                SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING
            }
        } else {
            getDefaultSortByOld(orderStatusFilterKey)
        }
    }

    private fun getDefaultSortByOld(orderStatusFilterKey: String): Long {
        return if (orderStatusFilterKey == SomConsts.KEY_CONFIRM_SHIPPING) {
            SomConsts.SORT_BY_PAYMENT_DATE_ASCENDING
        } else {
            SomConsts.SORT_BY_PAYMENT_DATE_DESCENDING
        }
    }
}
