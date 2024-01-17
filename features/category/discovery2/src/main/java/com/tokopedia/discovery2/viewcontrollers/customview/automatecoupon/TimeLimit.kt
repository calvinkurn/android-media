package com.tokopedia.discovery2.viewcontrollers.customview.automatecoupon

import java.util.Date

data class TimeLimit(
    val prefix: DynamicColorText?,
    val endDate: Date?,
    val endText: String?
) {

    val availableFormat: AvailableFormat
        get() = getFormat()

    fun isAvailable(): Boolean {
        return !prefix?.value.isNullOrEmpty() && (endDate != null || !endText.isNullOrEmpty())
    }

    private fun getFormat(): AvailableFormat {
        return if (endDate != null) AvailableFormat.TIMER
        else AvailableFormat.TEXT
    }

    enum class AvailableFormat {
        TEXT,
        TIMER
    }
}
