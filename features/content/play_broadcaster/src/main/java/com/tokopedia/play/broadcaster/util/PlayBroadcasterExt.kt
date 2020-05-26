package com.tokopedia.play.broadcaster.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

/**
 * Created by jegul on 26/05/20
 */
internal inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}