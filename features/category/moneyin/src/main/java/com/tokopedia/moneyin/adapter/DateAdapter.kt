package com.tokopedia.moneyin.adapter

import com.tokopedia.datepicker.numberpicker.NumberPickerAdapter
import com.tokopedia.kotlin.extensions.view.clamp

class DateAdapter(var date: ArrayList<String> = arrayListOf()) : NumberPickerAdapter() {

    fun getMaxIndex(): Int = getSize() - 1

    fun getMinIndex(): Int = 0

    override fun getPosition(vale: String): Int = date.indexOf(vale).clamp(getMinIndex(), getMaxIndex())

    override fun getTextWithMaximumLength(): String = date.maxBy { it.length } ?: ""

    override fun getValue(position: Int): String {
        if (position >= getMinIndex() && position <= getMaxIndex())
            return date[position]

        if (position <= getMaxIndex())
            return date[position + getSize()]

        if (position >= getMinIndex())
            return date[position - getSize()]

        return ""
    }

    override fun getSize(): Int = date.size
}