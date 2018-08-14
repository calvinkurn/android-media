package com.tokopedia.topads.common.view.listener

import android.view.View
import android.widget.Checkable

interface RadioCheckable: Checkable {
    fun addOnCheckChangeListener(listener: OnCheckedChangeListener)
    fun removeOnCheckChangeListener(listener: OnCheckedChangeListener)

    interface OnCheckedChangeListener {
        fun onCheckedChanged(radioGroup: View, isChecked: Boolean)
    }
}