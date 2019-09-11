package com.tokopedia.productcard.utils

import android.support.annotation.DimenRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.TextView
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardView

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

fun TextView.setValueWithConfig(textValue: String, blankSpaceConfigValue: Boolean) {
    if (textValue.isNotEmpty()) {
        text = textValue
        visibility = View.VISIBLE
    }
    else {
        visibility = if (blankSpaceConfigValue) {
            View.INVISIBLE
        } else {
            View.GONE
        }
    }
}