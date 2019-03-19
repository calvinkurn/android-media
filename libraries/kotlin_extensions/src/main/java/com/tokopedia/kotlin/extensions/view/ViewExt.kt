package com.tokopedia.kotlin.extensions.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.support.annotation.DimenRes
import android.support.annotation.StringRes
import android.view.*
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.kotlin.extensions.R
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 30/11/18.
 */

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showWithCondition(shouldShow: Boolean) {
    this.visibility = if (shouldShow) View.VISIBLE else View.GONE
}

fun View.shouldShowWithAction(shouldShow: Boolean, action: () -> Unit) {
    if (shouldShow) {
        show()
        action()
    } else {
        hide()
    }
}

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

fun View.showErrorToaster(errorMessage: String, @StringRes actionMessage: Int = R.string.title_try_again, action: () -> Unit) {
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

fun View.showNormalToaster(successMessage: String) {
    this.showNormalToaster(successMessage, null as String?) { }
}

fun View.showNormalToaster(successMessage: String, @StringRes actionMessage: Int = R.string.title_ok, action: () -> Unit) {
    this.showNormalToaster(successMessage, context.getString(actionMessage), action)
}

fun View.showNormalToaster(successMessage: String, actionMessage: String?, action: () -> Unit) {
    val toaster = ToasterNormal.make(this, successMessage, BaseToaster.LENGTH_LONG)
    actionMessage?.let { message ->
        toaster.setAction(message) {
            action()
        }
    }
    toaster.show()
}

fun View.showEmptyState(@StringRes errorMessage: Int, action: () -> Unit) {
    this.showEmptyState(context.getString(errorMessage), action)
}

fun View.showEmptyState(errorMessage: String, action: () -> Unit) {
    NetworkErrorHelper.showEmptyState(this.context, this, errorMessage) {
        action()
    }
}

fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(left, top, right, bottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = left
        layoutParams.marginEnd = right
    }
}

fun View.getDimens(@DimenRes id: Int): Int {
    return this.context.resources.getDimension(id).toInt()
}

fun View.addOnImpressionListener(holder: ImpressHolder, listener: ViewHintListener) {
    impressHolder = holder;
    hintListener = listener;
    if (impressHolder != null && !impressHolder!!.isInvoke) {
        invoke(this)
    }
}

fun View.addOnImpressionListener(holder: ImpressHolder?, listener: ViewHintListener) {
    viewTreeObserver.addOnScrollChangedListener(
            object : ViewTreeObserver.OnScrollChangedListener {
                override fun onScrollChanged() {
                    if (!holder!!.isInvoke && listener != null) {
                        listener.onViewHint()
                        holder.invoke()
                    }
                    viewTreeObserver.removeOnScrollChangedListener(this)
                }
            })
}

interface ViewHintListener {
    fun onViewHint()
}
