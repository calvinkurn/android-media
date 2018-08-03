package com.tokopedia.topads.common.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflateLayout(layoutId: Int, isAttached: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, isAttached)
}