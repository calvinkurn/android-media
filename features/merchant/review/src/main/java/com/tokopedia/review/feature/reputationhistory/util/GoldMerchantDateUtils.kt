package com.tokopedia.review.feature.reputationhistory.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import java.util.ArrayList

object GoldMerchantDateUtils {
    fun getDateWithYear(date: String, monthNames: Array<String>?): String {
        return try {
            val dateRaw = getDateRaw(date)
            var month = dateRaw.getOrNull(1)
            month = monthNames?.getOrNull(month.toIntOrZero() - 1).orEmpty()
            val day = dateRaw.getOrNull(0).orEmpty()
            "$day $month ${dateRaw.getOrNull(2)}"
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getDateRaw(label: String, monthNames: Array<String>?): String {
        val split = label.split(" ".toRegex()).toTypedArray()
        return split.getOrNull(0) + " " + monthNames?.getOrNull(split.getOrNull(1).toIntOrZero() - 1)
    }

    private fun getDateRaw(s: String): List<String> {
        val result: MutableList<String> = ArrayList<String>()
        val year = s.substring(YEAR_START_INDEX, YEAR_END_INDEX)
        val month = s.substring(MONTH_START_INDEX, MONTH_END_INDEX)
        val day = s.substring(DAY_END_INDEX)
        result.add(day)
        result.add(month)
        result.add(year)
        return result
    }

    private const val YEAR_START_INDEX = 0
    private const val YEAR_END_INDEX = 4

    private const val MONTH_START_INDEX = 4
    private const val MONTH_END_INDEX = 6

    private const val DAY_END_INDEX = 6

}