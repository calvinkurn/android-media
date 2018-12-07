package com.tokopedia.kotlin.extensions.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.R

/**
 * @author by milhamj on 30/11/18.
 */

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.shouldShowWithAction(shouldShow: Boolean, action: () -> Unit) {
    if (shouldShow) {
        visible()
        action()
    } else {
        gone()
    }
}

fun View.showLoading() {
    try {
        this.findViewById<View>(R.id.loadingView)!!.visible()
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
        this.findViewById<View>(R.id.loadingView)!!.gone()
    } catch (e: NullPointerException) {
        e.debugTrace()
    }
}