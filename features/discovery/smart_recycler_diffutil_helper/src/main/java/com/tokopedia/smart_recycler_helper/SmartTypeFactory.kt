package com.tokopedia.smart_recycler_helper

import android.view.View

interface SmartTypeFactory {
    fun createViewHolder(view: View, type: Int): SmartAbstractViewHolder<*>
}