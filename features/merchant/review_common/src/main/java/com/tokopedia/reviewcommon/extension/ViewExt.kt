package com.tokopedia.reviewcommon.extension

import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager

fun ConstraintSet.topToTopOf(@IdRes startSide: Int, @IdRes endSide: Int) {
    connect(startSide, ConstraintSet.TOP, endSide, ConstraintSet.TOP)
}

fun ConstraintSet.bottomToTopOf(@IdRes startSide: Int, @IdRes endSide: Int) {
    connect(startSide, ConstraintSet.BOTTOM, endSide, ConstraintSet.TOP)
}

fun ConstraintSet.clearTop(@IdRes id: Int) {
    clear(id, ConstraintSet.TOP)
}

fun ConstraintSet.clearBottom(@IdRes id: Int) {
    clear(id, ConstraintSet.BOTTOM)
}

fun ConstraintLayout.changeConstraint(transformer: ConstraintSet.() -> Unit) {
    ConstraintSet().run {
        clone(this@changeConstraint)
        transformer(this)
        applyTo(this@changeConstraint)
    }
    TransitionManager.beginDelayedTransition(this)
}