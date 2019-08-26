package com.tokopedia.productcard.utils

import android.support.annotation.DimenRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View

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