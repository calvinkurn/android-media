package com.tokopedia.productcard.utils

import androidx.annotation.DimenRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker

internal val View.isVisible: Boolean
    get() = visibility == View.VISIBLE

internal val View?.isNotNullAndVisible
    get() = this != null && this.isVisible

internal val View?.isNullOrNotVisible
    get() = this == null || !this.isVisible

internal fun View.doIfVisible(action: (View) -> Unit) {
    if(this.isVisible) {
        action(this)
    }
}

internal fun View.getDimensionPixelSize(@DimenRes id: Int): Int {
    return this.context.resources.getDimensionPixelSize(id)
}

internal fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
    this?.let {
        val constraintSet = ConstraintSet()

        constraintSet.clone(it)
        configureConstraintSet(constraintSet)
        constraintSet.applyTo(it)
    }
}

internal fun TextView?.setTextWithBlankSpaceConfig(textValue: String, blankSpaceConfigValue: Boolean) {
    this?.configureVisibilityWithBlankSpaceConfig(textValue.isNotEmpty(), blankSpaceConfigValue) {
        it.text = MethodChecker.fromHtml(textValue)
    }
}

internal fun <T: View> T?.configureVisibilityWithBlankSpaceConfig(isVisible: Boolean, blankSpaceConfigValue: Boolean, action: (T) -> Unit) {
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

internal fun <T: View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
    if (this == null) return

    if (shouldShow) {
        this.visibility = View.VISIBLE
        action(this)
    } else {
        this.visibility = View.GONE
    }
}

internal operator fun Boolean.divAssign(toCompare: Boolean) {
    this || toCompare
}