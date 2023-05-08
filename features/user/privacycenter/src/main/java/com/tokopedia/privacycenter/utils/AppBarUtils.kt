package com.tokopedia.privacycenter.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.utils.view.binding.internal.findRootView
import com.tokopedia.privacycenter.R

fun Activity.getResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Activity.getDynamicColorStatusBar(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        MethodChecker.getColor(
            this,
            com.tokopedia.unifyprinciples.R.color.Unify_Background
        )
    } else {
        Color.GRAY
    }
}

// This method has no effect on API < 23.
fun Activity.setTextStatusBar(setToWhite: Boolean) {
    WindowInsetsControllerCompat(window, findRootView(this)).isAppearanceLightStatusBars = !setToWhite
}

// true for white, false for black
fun Activity.getIconBackWithColor(getWhite: Boolean): Drawable? {
    return getIconUnifyDrawable(
        this,
        IconUnify.ARROW_BACK,
        this.getIdColor(getWhite)
    )
}

// true for white, false for black
fun Activity.getIdColor(getWhite: Boolean): Int {
    return ContextCompat.getColor(
        this,
        getColor(getWhite)
    )
}

// true for white, false for black
fun getColor(getWhite: Boolean): Int {
    return if (getWhite) {
        R.color.privacycenter_dms_white
    } else {
        R.color.privacycenter_dms_black
    }
}
