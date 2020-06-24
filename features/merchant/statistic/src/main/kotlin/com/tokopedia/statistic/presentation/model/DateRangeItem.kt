package com.tokopedia.statistic.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.statistic.presentation.view.adapter.factory.DateRangeAdapterFactory
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

sealed class DateRangeItem(
        open val label: String = "",
        open val startDate: Date? = null,
        open val endDate: Date? = null,
        open var isSelected: Boolean = false,
        open val type: Int
) : Visitable<DateRangeAdapterFactory> {

    companion object {
        const val TYPE_TODAY = 0
        const val TYPE_LAST_7_DAYS = 1
        const val TYPE_LAST_30_DAYS = 2
        const val TYPE_PER_DAY = 3
        const val TYPE_PER_WEEK = 4
        const val TYPE_PER_MONTH = 5
        const val TYPE_BUTTON = 6
    }

    data class Click(
            override val label: String,
            override val startDate: Date,
            override val endDate: Date,
            override var isSelected: Boolean = false,
            override val type: Int
    ) : DateRangeItem(label, startDate, endDate, isSelected, type) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Pick(
            override val label: String,
            override var startDate: Date? = null,
            override var endDate: Date? = null,
            override var isSelected: Boolean = false,
            override val type: Int
    ) : DateRangeItem(label, startDate, endDate, isSelected, type) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object ApplyButton : DateRangeItem(type = TYPE_BUTTON) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}