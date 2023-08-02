package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.dpToPx

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselLayoutManager(
    context: Context
) : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {

    private val maxWidth = 234.dpToPx(context.resources.displayMetrics)

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        if (lp != null) {
            lp.width = (0.65 * width).toInt().coerceAtMost(maxWidth)
            lp.height = (1 / 0.5625 * lp.width).toInt()
        }
        return super.checkLayoutParams(lp)
    }
}
