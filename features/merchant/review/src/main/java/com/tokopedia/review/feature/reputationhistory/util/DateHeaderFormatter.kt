package com.tokopedia.review.feature.reputationhistory.util

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils


/**
 * Created by normansyahputa on 3/29/17.
 */
open class DateHeaderFormatter(var monthNames: Array<String>?) {
    fun getDateWithoutYearFormat(sDate: Long): String {
        validateMonths()
        val dateFormatForInput = DateFormatUtils.getFormattedDate(sDate, DD_MM)
        return GoldMerchantDateUtils.getDateRaw(dateFormatForInput, monthNames)
    }

    protected fun validateMonths() {
        check(!(monthNames.isNullOrEmpty())) { "need to supply valid month name !!" }
    }

    fun getEndDateFormat(eDate: Long): String {
        validateMonths()
        return GoldMerchantDateUtils.getDateWithYear(
            DateFormatUtils.getFormattedDate(
                eDate,
                YYYY_M_MDD
            ), monthNames
        )
    }

    companion object {
        const val YYYY_M_MDD = "yyyyMMdd"
        const val DD_MM = "dd MM"
    }
}