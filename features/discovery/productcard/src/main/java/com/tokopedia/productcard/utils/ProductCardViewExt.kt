package com.tokopedia.productcard.utils

import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

val View?.isNotNullAndVisible
    get() = this != null && this.isVisible

val View?.isNullOrNotVisible
    get() = this == null || !this.isVisible

fun View.doIfVisible(action: (View) -> Unit) {
    if(this.isVisible) {
        action(this)
    }
}

fun View.getDimensionPixelSize(@DimenRes id: Int): Int {
    return this.context.resources.getDimensionPixelSize(id)
}

fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
    this?.let {
        val constraintSet = ConstraintSet()

        constraintSet.clone(it)
        configureConstraintSet(constraintSet)
        constraintSet.applyTo(it)
    }
}

fun TextView?.setTextWithBlankSpaceConfig(textValue: String, blankSpaceConfigValue: Boolean) {
    this?.configureVisibilityWithBlankSpaceConfig(textValue.isNotEmpty(), blankSpaceConfigValue) {
        it.text = MethodChecker.fromHtml(textValue)
    }
}

fun <T: View> T?.configureVisibilityWithBlankSpaceConfig(isVisible: Boolean, blankSpaceConfigValue: Boolean, action: (T) -> Unit) {
    if (this == null) return

    visibility = if (isVisible) {
        action(this)
        View.VISIBLE
    } else {
        getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue)
    }
}

private fun getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue: Boolean): Int {
    return if (blankSpaceConfigValue) {
        View.INVISIBLE
    }
    else {
        View.GONE
    }
}