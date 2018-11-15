package com.tokopedia.kol.common.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.common.utils.GlobalConfig

/**
 * @author by milhamj on 15/11/18.
 */

fun View.showLoading() {
    try {
        this.findViewById<View>(R.id.loadingView).show()
    } catch (e: NullPointerException) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        params.weight = 1.0f
        inflater.inflate(R.layout.partial_loading_layout, this as ViewGroup)
    }
}

fun View.hideLoading() {
    try {
        this.findViewById<View>(R.id.loadingView).hide()
    } catch (e: NullPointerException) {
        e.debugTrace()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun Throwable.debugTrace() {
    if (GlobalConfig.isAllowDebuggingTools()) printStackTrace()
}