package com.tokopedia.tokopedianow.common.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.searchbar.helper.ViewHelper

class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        val show = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        layoutParams?.height = ViewHelper.getStatusBarHeight(context)
        visibility = if (show) VISIBLE else INVISIBLE
        setTheme(transparent = true)
    }

    fun setTheme(transparent: Boolean) {
        val color = if (transparent) {
            ContextCompat.getColor(context, android.R.color.transparent)
        } else {
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
        setBackgroundColor(color)
    }
}