package com.tokopedia.tradein.adapter

import com.tokopedia.kotlin.extensions.view.clamp
import com.tokopedia.profilecompletion.addbod.view.widget.numberpicker.NumberPickerAdapter

class TimeAdapter(var time: ArrayList<String> = arrayListOf()) : NumberPickerAdapter() {

    fun getMaxIndex(): Int = getSize() - 1

    fun getMinIndex(): Int = 0

    override fun getPosition(vale: String): Int = time.indexOf(vale).clamp(getMinIndex(), getMaxIndex())

    override fun getTextWithMaximumLength(): String = time.maxBy { it.length } ?: ""

    override fun getValue(position: Int): String {
        if (position >= getMinIndex() && position <= getMaxIndex())
            return time[position]

        if (position <= getMaxIndex())
            return time[position + getSize()]

        if (position >= getMinIndex())
            return time[position - getSize()]

        return ""
    }

    override fun getSize(): Int = time.size
}
