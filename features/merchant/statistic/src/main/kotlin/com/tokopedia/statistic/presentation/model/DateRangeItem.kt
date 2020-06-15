package com.tokopedia.statistic.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.statistic.presentation.view.adapter.factory.DateRangeAdapterFactory
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

sealed class DateRangeItem(
        open val label: String,
        open val startDate: Date,
        open val endDate: Date,
        open var isSelected: Boolean = false
) : Visitable<DateRangeAdapterFactory> {

    data class Default(
            override val label: String,
            override val startDate: Date,
            override val endDate: Date,
            override var isSelected: Boolean = false
    ) : DateRangeItem(label, startDate, endDate, isSelected) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Custom(
            override val label: String,
            override val startDate: Date,
            override val endDate: Date,
            override var isSelected: Boolean = false
    ) : DateRangeItem(label, startDate, endDate, isSelected) {

        override fun type(typeFactory: DateRangeAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}