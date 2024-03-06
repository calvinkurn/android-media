package com.tokopedia.discovery_component.widgets.automatecoupon

import java.util.Date

sealed class TimeLimit(
    open val prefix: DynamicColorText?
) {
    data class Timer(
        override val prefix: DynamicColorText?,
        val endDate: Date?
    ) : TimeLimit(prefix) {
        override fun isAvailable(): Boolean {
            return !prefix?.value.isNullOrEmpty() && endDate != null
        }
    }

    data class Text(
        override val prefix: DynamicColorText?,
        val endText: String?
    ) : TimeLimit(prefix) {
        override fun isAvailable(): Boolean {
            return !prefix?.value.isNullOrEmpty() && !endText.isNullOrEmpty()
        }
    }

    abstract fun isAvailable(): Boolean
}
