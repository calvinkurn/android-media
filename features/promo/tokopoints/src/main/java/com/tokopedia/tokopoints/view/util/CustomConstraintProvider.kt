package com.tokopedia.tokopoints.view.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

object CustomConstraintProvider {

    fun setCustomConstraint(view: View, parentId: Int, baseId: Int, anchorId: Int, constraint: Int) {
        val constraintLayout: ConstraintLayout = view.findViewById(parentId)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(baseId, constraint, anchorId, constraint, 0)
        constraintSet.applyTo(constraintLayout)
    }
}