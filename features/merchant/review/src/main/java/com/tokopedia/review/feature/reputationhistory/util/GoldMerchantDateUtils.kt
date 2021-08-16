package com.tokopedia.review.feature.reputationhistory.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import java.util.ArrayList

object GoldMerchantDateUtils {
    fun getDateWithYear(date: String, monthNames: Array<String>?): String {
        val dateRaw = getDateRaw(date)
        var month = dateRaw[1]
        month = monthNames?.getOrNull(month.toIntOrZero() - 1).orEmpty()
        val day = Integer.valueOf(dateRaw[0]).toString()
        return "$day $month ${dateRaw[2]}"
    }

    fun getDateRaw(label: String, monthNames: Array<String>?): String {
        val split = label.split(" ".toRegex()).toTypedArray()
        return split[0] + " " + monthNames?.getOrNull(split[1].toInt() - 1)
    }

    private fun getDateRaw(s: String): List<String> {
        val result: MutableList<String> = ArrayList<String>()
        val year = s.substring(0, 4)
        val month = s.substring(4, 6)
        val day = s.substring(6)
        result.add(day)
        result.add(month)
        result.add(year)
        return result
    }
}