package com.tokopedia.profilecompletion.addbod.view.widget.numberpicker

/**
 * Created by Ade Fulki on 2019-07-18.
 * ade.hadian@tokopedia.com
 */

abstract class NumberPickerAdapter {

    abstract fun getValue(position: Int): String

    abstract fun getPosition(vale: String): Int

    abstract fun getTextWithMaximumLength(): String

    open fun getSize(): Int = -1

    var picker: NumberPicker? = null

    fun notifyDataSetChanged() {
        picker?.invalidate()
        picker?.requestLayout()
    }
}