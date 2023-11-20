package com.tokopedia.content.common.ui.layoutmanager

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created By : Jonathan Darwin on July 24, 2023
 */
abstract class FocusedCarouselLayoutManager(
    context: Context
) : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {

    protected open val maxWidth = 234.dpToPx(context.resources.displayMetrics)
    protected open val widthPercentage = 0.65
    protected open val heightPercentage = 1 / 0.5625

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        if (lp != null) {
            lp.width = (widthPercentage * width).toInt().coerceAtMost(maxWidth)
            lp.height = (heightPercentage * lp.width).toInt()
        }
        return super.checkLayoutParams(lp)
    }
}

