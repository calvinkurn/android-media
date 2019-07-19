package com.tokopedia.profilecompletion.addbod.view.widget.datepicker

import com.tokopedia.kotlin.extensions.view.clamp
import com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker.NumberPickerAdapter
import java.text.DateFormatSymbols

class MonthAdapter : NumberPickerAdapter() {

    private val months = DateFormatSymbols.getInstance().months

    fun getMaxIndex(): Int = getSize() - 1

    fun getMinIndex(): Int = 0

    override fun getValue(position: Int): String {
        if (position >= getMinIndex() && position <= getMaxIndex())
            return months[position]

        if (position <= getMaxIndex())
            return months[position + getSize()]

        if (position >= getMinIndex())
            return months[position - getSize()]

        return ""
    }

    override fun getPosition(vale: String): Int = months.indexOf(vale).clamp(getMinIndex(), getMaxIndex())

    override fun getTextWithMaximumLength(): String = months.maxBy { it.length } ?: ""

    override fun getSize(): Int = months.size
}