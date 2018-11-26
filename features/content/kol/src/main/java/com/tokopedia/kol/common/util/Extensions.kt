package com.tokopedia.kol.common.util

import android.content.Context
import android.support.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.kol.R

/**
 * @author by milhamj on 15/11/18.
 */

fun View.showLoading() {
    try {
        this.findViewById<View>(R.id.loadingView)!!.show()
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
        this.findViewById<View>(R.id.loadingView)!!.hide()
    } catch (e: NullPointerException) {
        e.debugTrace()
    }
}

fun View.showErrorToaster(errorMessage: String) {
    this.showErrorToaster(errorMessage, null as String?) { }
}

fun View.showErrorToaster(errorMessage: String, @StringRes actionMessage: Int, action: () -> Unit) {
    this.showErrorToaster(errorMessage, context.getString(actionMessage), action)
}

fun View.showErrorToaster(errorMessage: String, actionMessage: String?, action: () -> Unit) {
    val toaster = ToasterError.make(this, errorMessage, BaseToaster.LENGTH_LONG)
    actionMessage?.let { message ->
        toaster.setAction(message) {
            action()
        }
    }
    toaster.show()
}

fun View.showNormalToaster(errorMessage: String) {
    this.showNormalToaster(errorMessage, null as String?) { }
}

fun View.showNormalToaster(errorMessage: String, @StringRes actionMessage: Int, action: () -> Unit) {
    this.showNormalToaster(errorMessage, context.getString(actionMessage), action)
}

fun View.showNormalToaster(errorMessage: String, actionMessage: String?, action: () -> Unit) {
    val toaster = ToasterNormal.make(this, errorMessage, BaseToaster.LENGTH_LONG)
    actionMessage?.let { message ->
        toaster.setAction(message) {
            action()
        }
    }
    toaster.show()
}

fun View.showEmptyState(@StringRes errorMessage: Int, action: () -> Unit) {
    NetworkErrorHelper.showEmptyState(this.context, this, context.getString(errorMessage)) {
        action()
    }
}

fun View.showEmptyState(errorMessage: String, action: () -> Unit) {
    NetworkErrorHelper.showEmptyState(this.context, this, errorMessage) {
        action()
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable?.toString() ?: "")
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

fun Throwable.debugTrace() {
    if (GlobalConfig.isAllowDebuggingTools()) printStackTrace()
}