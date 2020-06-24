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
        open var isSelected: Boolean = false
) : Visitable<DateRangeAdapterFactory> {

    data class Click(
            override val label: String,
            override val startDate: Date,
            override val endDate: Date,
            override var isSelected: Boolean = false
    ) : DateRangeItem(label, startDate, endDate, isSelected) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Pick(
            override val label: String,
            override var startDate: Date? = null,
            override var endDate: Date? = null,
            override var isSelected: Boolean = false,
            val isSingleDateMode: Boolean = false,
            val type: Int
    ) : DateRangeItem(label, startDate, endDate, isSelected) {

        companion object {
            const val TYPE_PER_DAY = 0
            const val TYPE_PER_WEEK = 1
            const val TYPE_PER_MONTH = 2
        }

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    object ApplyButton : DateRangeItem() {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}